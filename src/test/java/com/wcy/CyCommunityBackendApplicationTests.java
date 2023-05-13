package com.wcy;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcy.mapper.BmsCommentMapper;
import com.wcy.model.dto.CommentDTO;
import com.wcy.model.entity.BmsComment;
import com.wcy.model.entity.BmsCommentFollow;
import com.wcy.model.entity.QuestionTag;
import com.wcy.model.vo.CommentVO;
import com.wcy.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class CyCommunityBackendApplicationTests {

//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Test
//    void contextLoads() {
////        CommentDTO dto = new CommentDTO("ddd","大hi看来我你疯啦是否");
////        String s = JSON.toJSONString(dto);
////        redisService.set("1",s,60);
////        String s1 = (String) redisService.get("1");
////        CommentDTO commentDTO = JSON.parseObject(s1, CommentDTO.class);
////        System.out.println(commentDTO.getContent());
//        System.out.println(redisService.exists("1"));
//    }
//
//
//
//    @Test
//    void sendEmail(){
//        emailService.sendCode("351213273@qq.com","欢迎注册","点击下面连接进行激活,有效期10分钟：http://8.130.113.22:8001/#/ums/user/active?code="+1);
//    }
//
//
//    @Test
//    void snowFake(){
//
//        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
//        for (int i=0;i<10;i++) {
//            String nextIdStr = snowflake.nextIdStr();
//            System.out.println(nextIdStr);
//        }
//    }


    @Autowired
    BmsCommentMapper bmsCommentMapper;

    @Autowired
    IBmsCommentFollowService iBmsCommentFollowService;

    @Autowired
    IBmsCommentService iBmsCommentService;
    @Test
    void comments(){
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String code = snowflake.nextIdStr();
        System.out.println(code);
    }

    @Autowired
    private QuestionTagService  questionTagService;
    @Test
    void questionTAg(){
        for(int i =0 ;i<40;i++){
            QuestionTag tag = QuestionTag.builder()
                    .name("test")
                    .questionCount(5)
                    .build();
            questionTagService.save(tag);
        }
    }
}
