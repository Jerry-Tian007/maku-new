package com.ruoyi.gpt.model.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色模型资源对象 model_resource_role
 *
 * @author Eric
 * @date 2024-07-22
 */
@Data
public class ModelResourceRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 模板ID
     */
    private Long modelResourceTemplateId;

}
