package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 角色模型资源对象 model_resource_role
 * 
 * @author Eric
 * @date 2024-07-22
 */
@Data
public class ModelResourceClient extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    /** id */
    private Long id;

    private String name;

    private String client;

    private String status;
}
