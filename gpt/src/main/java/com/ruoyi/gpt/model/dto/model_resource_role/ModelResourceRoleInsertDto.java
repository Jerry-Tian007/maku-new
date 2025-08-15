package com.ruoyi.gpt.model.dto.model_resource_role;

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
public class ModelResourceRoleInsertDto {
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 模型id列表
     */
    private Long[] modelResourceTemplateIds;

}
