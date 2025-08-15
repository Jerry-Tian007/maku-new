package com.ruoyi.gpt.model.dto.graph;

import lombok.Data;

import java.util.List;

@Data
public class GraphAuthDto {

    private List<String> graphIdList;

    private Long roleId;

}
