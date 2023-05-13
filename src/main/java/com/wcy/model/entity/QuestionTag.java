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
 * @since 2023-05-13 05:11:46
 */
@Data
@Builder   //支持链式操作
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("question_tag")
public class QuestionTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 标签名
     */
    private String name;

    /**
     * 所在标签下的问题量
     */
    private Integer questionCount;
}
