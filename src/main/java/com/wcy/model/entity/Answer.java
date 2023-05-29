package com.wcy.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:10:31
 */
@Data
@Builder   //支持链式操作
@Accessors(chain = true)
@TableName("answer")
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {

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
     * 问题ID
     */
    private String questionId;

    /**
     * 内容
     */
    private String content;

    /**
     * 喜欢量
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
