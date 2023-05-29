package com.wcy.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateQuestionDTO implements Serializable {

    private static final long serialVersionUID = -5957433707110390852L;

    /**
     * 内容
     */
    private String question;

    /**
     * 标签
     */
    private List<String> tags;
}
