package com.ruoyi.gpt.model.vo.prompt_library;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class PromptLibrarySelectListVo {

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

}
