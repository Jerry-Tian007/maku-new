package com.ruoyi.gpt.model.vo.model_resource_role;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色模型资源对象 model_resource_role
 * 
 * @author Eric
 * @date 2024-07-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelResourceRoleListVo
{
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private  Long roleId;
    /** 角色id */
    @Excel(name = "角色id")
    private String roleName;
    /** 模型id */
    @Excel(name = "模型id")
    private List<String> modelResourceTypes;

}
