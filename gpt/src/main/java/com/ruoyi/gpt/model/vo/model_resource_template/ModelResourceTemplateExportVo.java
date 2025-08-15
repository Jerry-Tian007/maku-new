package com.ruoyi.gpt.model.vo.model_resource_template;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 模型资源模板管理对象 model_resource_template
 *
 * @author Eric
 * @date 2024-07-15
 */
@Data
public class ModelResourceTemplateExportVo {
    private static final long serialVersionUID = 1L;

    /**
     * 模型类型
     */
    @Excel(name = "模型类型")
    private String type;

    /**
     * 模型模板
     */
    @Excel(name = "模型模板")
    private String template;

}
