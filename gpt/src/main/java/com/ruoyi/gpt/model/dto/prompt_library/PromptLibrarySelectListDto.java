package com.ruoyi.gpt.model.dto.prompt_library;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class PromptLibrarySelectListDto {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;


}
