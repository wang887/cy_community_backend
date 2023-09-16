package com.wcy;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.wcy.jwt.JwtUtil;
import com.wcy.mapper.BmsCommentMapper;
import com.wcy.model.entity.Answer;
import com.wcy.model.entity.Question;
import com.wcy.model.entity.QuestionRelationTag;
import com.wcy.model.entity.QuestionTag;
import com.wcy.service.*;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;


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

    @Test
    void bcrypt(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("wcy123"));
    }
    @Test
    void parseToken(){
        Map<String, Object> map = JwtUtil.paraseToken("eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyTmFtZSI6IjE2NTgwOTEyNzQzNjU5MDI4NTAiLCJleHAiOjE2ODg5NDU1MTh9.FmOMMDecUkKmBL2-IG4yA6-s62AyiPky7FqWpefX_RsfzYd3gmm8yVyquL6yXqy6a3cgwgyzmPX-W9FCWDa-Pg");

        System.out.println(map.get("userName"));
    }

    @Autowired
    private QuestionTagService  questionTagService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRelationTagService questionRelationTagService;

    @Autowired
    private AnswerService answerService;
    @Test
    void questionTAg(){
//        Question question = Question.builder()
//                .content("浅谈对男欢女爱的理解？")
//                .userId("1653683754269786113")
//                .answers(0)
//                .collections(0)
//                .followers(0)
//                .createTime(new Date())
//                .build();
//
//        questionService.save(question);

//        QuestionTag tag = QuestionTag.builder()
//                .name("爱情")
//                .questionCount(1)
//                .build();
//        questionTagService.save(tag);

//        QuestionRelationTag questionRelationTag = QuestionRelationTag.builder()
//                .tagId("1657376190786215938")
//                .questionId("1657375564144689154")
//                .build();

//        Answer answer = Answer.builder()
//                .questionId("1657375329435639810")
//                .userId("1655456426061000705")
//                .content("java是世界上最好的语言")
//                .createTime(new Date())
//                .support(2)
//                .build();
//
//        answerService.save(answer);
    }
}
