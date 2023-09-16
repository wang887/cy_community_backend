package com.wcy.service.Impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vdurmont.emoji.EmojiParser;
import com.wcy.mapper.BmsTagMapper;
import com.wcy.mapper.BmsTopicMapper;
import com.wcy.mapper.BmsTopicTagMapper;
import com.wcy.mapper.BmsUmsUserMapper;
import com.wcy.model.dto.CreateTopicDTO;
import com.wcy.model.dto.UpdateTopicDTO;
import com.wcy.model.entity.BmsTag;
import com.wcy.model.entity.BmsTopic;
import com.wcy.model.entity.BmsTopicTag;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.PostVO;
import com.wcy.model.vo.ProfileVO;
import com.wcy.service.IBmsTagService;
import com.wcy.service.IBmsTopicService;
import com.wcy.service.IBmsTopicTagService;
import com.wcy.service.IBmsUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IBmsTopicServiceImpl extends ServiceImpl<BmsTopicMapper, BmsTopic>
                    implements IBmsTopicService {
    @Resource
    private BmsTagMapper bmsTagMapper;


    @Resource
    private BmsUmsUserMapper bmsUmsUserMapper;

    @Resource
    private BmsTopicTagMapper bmsTopicTagMapper;

    @Resource
    //懒加载
    @Lazy
    private IBmsTagService iBmsTagService;

    @Resource
    private IBmsTopicTagService iBmsTopicTagService;


    @Resource
    private IBmsUmsUserService iBmsUmsUserService;
    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.selectListAndPage(page, tab);
        // 查询话题的标签
        iPage.getRecords().forEach(topic -> {
            List<BmsTopicTag> topicTags = iBmsTopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<String> tagIds = topicTags.stream().map(BmsTopicTag::getTagId).collect(Collectors.toList());
                List<BmsTag> tags = bmsTagMapper.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BmsTopic create(CreateTopicDTO dto, UmsUser user) {
        BmsTopic topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsTopic>().eq(BmsTopic::getTitle, dto.getTitle()));
//        Assert.isNull(topic1, "话题已存在，请修改");    //此处的话题应该指的是title,可以重复

        // 封装
        BmsTopic topic = BmsTopic.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .build();
        this.baseMapper.insert(topic);

        // 用户积分增加
        int newScore = user.getScore() + 1;
        bmsUmsUserMapper.updateById(user.setScore(newScore));

        // 标签
        if (!ObjectUtils.isEmpty(dto.getTags())) {
            // 保存标签
            List<BmsTag> tags = iBmsTagService.insertTags(dto.getTags());
            // 处理标签与话题的关联
            iBmsTopicTagService.createTopicTag(topic.getId(), tags);
        }

        return topic;
    }

    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        BmsTopic topic = this.baseMapper.selectById(id);
        Assert.notNull(topic, "当前话题不存在,或已被作者删除");
        // 查询话题详情
        topic.setView(topic.getView() + 1);
        this.baseMapper.updateById(topic);
        // emoji转码
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        map.put("topic", topic);
        // 标签
        QueryWrapper<BmsTopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmsTopicTag::getTopicId, topic.getId());
        Set<String> set = new HashSet<>();
        for (BmsTopicTag articleTag : iBmsTopicTagService.list(wrapper)) {
            set.add(articleTag.getTagId());
        }
        List<BmsTag> tags = iBmsTagService.listByIds(set);
        map.put("tags", tags);

        // 作者

        ProfileVO user = iBmsUmsUserService.getUserProfile(topic.getUserId());
        map.put("user", user);

        return map;
    }

    @Override
    public List<BmsTopic> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }

    @Override
    public Page<PostVO> searchByKey(String keyword, Page<PostVO> page) {
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.searchByKey(page, keyword);
        // 查询话题的标签
        setTopicTags(iPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateTopicDTO update(UpdateTopicDTO dto) {
        BmsTopic topic = dto.getTopic();
        List<String> tags = dto.getTags();
        topic.setModifyTime(new Date());
        topic.setContent(EmojiParser.parseToAliases(topic.getContent()));
        baseMapper.updateById(topic);
        /**
         * 修改tag
         * 1.插入新的tag
         * 2.找出不在tags内的标签与topic 删除
         * 3.添加topic与tag的对应
         */
        for (String tag:tags){
            BmsTag one = iBmsTagService.getOne(new LambdaQueryWrapper<BmsTag>().eq(BmsTag::getName, tag));
            if(ObjectUtils.isEmpty(one)) {
                BmsTag t = BmsTag.builder()
                        .name(tag)
                        .topicCount(0)
                        .build();
                iBmsTagService.save(t);
            }
        }
        List<BmsTopicTag> list = iBmsTopicTagService.list(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId, topic.getId()));
        for(BmsTopicTag bmsTopicTag : list){
            BmsTag one = iBmsTagService.getOne(new LambdaQueryWrapper<BmsTag>().eq(BmsTag::getId, bmsTopicTag.getTagId()));
            if(!tags.contains(one)){
                iBmsTopicTagService.remove(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getId,bmsTopicTag.getId()));
            }
            //对应的标签-1
            one.setTopicCount(one.getTopicCount()-1);
            iBmsTagService.updateById(one);
        }

        for (String tag:tags) {
            BmsTag one = iBmsTagService.getOne(new LambdaQueryWrapper<BmsTag>().eq(BmsTag::getName, tag));
            BmsTopicTag o = iBmsTopicTagService.getOne(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId,topic.getId()).eq(BmsTopicTag::getTagId,one.getId()));
            if(ObjectUtils.isEmpty(o)){
                BmsTopicTag t = BmsTopicTag.builder()
                        .topicId(topic.getId())
                        .tagId(one.getId())
                        .build();
                bmsTopicTagMapper.insert(t);
                one.setTopicCount(one.getTopicCount()+1);
                iBmsTagService.updateById(one);
            }
        }
        return dto;
    }

    private void setTopicTags(Page<PostVO> iPage) {
        iPage.getRecords().forEach(topic -> {
            List<BmsTopicTag> topicTags = iBmsTopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<String> tagIds = topicTags.stream().map(BmsTopicTag::getTagId).collect(Collectors.toList());
                List<BmsTag> tags = bmsTagMapper.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
    }
}
