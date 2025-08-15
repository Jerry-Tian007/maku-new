package com.ruoyi.gpt.model.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OaParams extends BaseEntity {
    /**
     * 业务id
     */
    @NotNull(message = "businessId不能为空")
    private Long businessId;
    /**
     * 业务名称
     */
    @NotNull
    @NotBlank(message = "businessName不能为空")
    private String businessName;
    /**
     * 推送类型
     */
    private String pushType;
    @Valid
    private List<OaFile> files;



}
