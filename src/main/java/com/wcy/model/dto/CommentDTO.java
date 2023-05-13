package com.wcy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;

    private String topic_id;

    /**
     * 内容
     */
    private String content;


    /**
     * 父评论ID
     */
    private String parents_id;

    /**
     * toUerId
     */
    private String user_id;







}
