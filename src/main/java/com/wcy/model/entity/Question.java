package com.wcy.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:09:25
 */
@Data
@Builder   //支持链式操作
@Accessors(chain = true)
@TableName("question")
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 回答量
     */
    private Integer answers;

    /**
     * 关注量
     */
    private Integer followers;

    /**
     * 收藏量
     */
    private Integer collections;

    /**
     * 点赞量
     */
    private Integer support;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;
}
