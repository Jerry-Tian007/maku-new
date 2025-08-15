package com.ruoyi.gpt.model.dto.model_resource_template;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 模型资源模板管理对象 model_resource_template
 *
 * @author Eric
 * @date 2024-07-15
 */
@Data
public class ModelResourceTemplateUpdateDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 模型类型
     */
    private String type;

    /**
     * 模型图标
     */
    private String icon;

    /**
     * 模型模板
     */
    private String template;
    /**
     * 场景
     */
    private String scene;

}
