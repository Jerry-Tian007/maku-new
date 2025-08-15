package com.ruoyi.gpt.model.dto.file;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 文件上传请求参数类
 *
 * @author tiankk
 */
@Data
public class UploadDto {
    /**
     * 是否更新 true：更新  false：不更新
     */
    private Boolean updateStatus;
    /**
     * 所属知识库id
     */
    @NotEmpty(message = "所属知识库不允许为空")
    private String kbIds;

}
