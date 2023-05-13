package com.wcy.model.dto;

import com.wcy.model.entity.BmsTopic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTopicDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;

    private BmsTopic topic;

    private List<String> tags;
}