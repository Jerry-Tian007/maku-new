package com.ruoyi.gpt.service;


import com.ruoyi.gpt.model.domain.ModelResourceClient;

import java.util.Set;

/**
 * 模型资源Client管理Service接口
 * 
 * @author Eric
 * @date 2024-07-09
 */
public interface IModelResourceClientService
{

    Set<String> selectModelResourceClientList();
}
