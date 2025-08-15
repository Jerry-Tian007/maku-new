package com.ruoyi.gpt.mapper;

import com.ruoyi.gpt.model.domain.ModelResourceClient;

import java.util.Set;

/**
 * 模型资源Client管理Mapper接口
 * 
 * @author Eric
 * @date 2024-07-09
 */
public interface ModelResourceClientMapper {

    Set<String> selectModelResourceClientList();
}
