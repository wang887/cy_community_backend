package com.wcy.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class CommentVO {

    private String id;

    private String content;

    private String topicId;

    private String userId;

    private String toUserId;

    private int support;      //点赞量

    private List<CommentVO> child = new ArrayList<>(); //待考虑

    private String username;   //回复者

    private String toUsername;  //被回复者


    private int replayNum; //回复数量，只有一级评论有

    private Date createTime;

}
