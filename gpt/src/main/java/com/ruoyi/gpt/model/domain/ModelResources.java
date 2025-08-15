package com.ruoyi.gpt.model.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 模型资源管理对象 model_resources
 *
 * @author Eric
 * @date 2024-07-09
 */
@Data
public class ModelResources extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 模型名称
     */
    @Excel(name = "模型名称")
    private String name;

    /**
     * 模型类型
     */
    @Excel(name = "模型类型")
    private String type;

    /**
     * 模型tpm
     */
    @Excel(name = "模型tpm")
    private Long tpm;

    /**
     * 模型参数
     */
    @Excel(name = "模型参数")
    private String parameter;

    /**
     * 删除者
     */
    @Excel(name = "删除者")
    private String deleteBy;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deleteTime;

    /**
     * 删除标识
     */
    private String delFlag;
    /**
     * 模型用途
     */
    @NotNull(message = "模型用途不能为空")
    private String scene;

}
