package com.ruoyi.gpt.model.dto.model_resources;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ModelResourcesUpdateDto {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空")
    private String name;

    /**
     * 模型类型
     */
    @NotBlank(message = "模型类型不能为空")
    private String type;

    /**
     * 模型tpm
     */
    @NotNull(message = "模型tpm不能为空")
    private Long tpm;

    /**
     * 模型参数
     */
    private String parameter;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模型用途
     */
    @NotNull(message = "模型用途不能为空")
    private String scene;
}
