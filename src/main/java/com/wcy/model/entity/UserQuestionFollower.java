package com.wcy.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wcy
 * @since 2023-05-23 11:28:27
 */
@Getter
@Setter
@TableName("user_question_follower")
public class UserQuestionFollower implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String questionId;

    private String userId;
}
