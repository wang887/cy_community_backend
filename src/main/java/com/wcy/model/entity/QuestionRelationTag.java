package com.wcy.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:12:09
 */
@Data
@Builder   //支持链式操作
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("question_relation_tag")
public class QuestionRelationTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 问题ID
     */
    private String questionId;

    /**
     * 标签ID
     */
    private String tagId;
}
