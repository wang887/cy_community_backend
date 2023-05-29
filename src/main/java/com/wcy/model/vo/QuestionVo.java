package com.wcy.model.vo;

import com.wcy.model.entity.BmsTag;
import com.wcy.model.entity.QuestionTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo implements Serializable {

    private static final long serialVersionUID = -261082150965211545L;

    private String id;

    private String userId;

    private String username;

    private String avatar;

    private String content;

    private int views;

    private  int collections;

    private int answers;

    private int followers;

    private int support;

    /**
     * 问题的话题标签积集合
     */
    private List<QuestionTag> tags;

    private Date createTime;
}
