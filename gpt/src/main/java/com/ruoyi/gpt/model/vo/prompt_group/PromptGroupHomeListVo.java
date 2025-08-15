package com.ruoyi.gpt.model.vo.prompt_group;

import com.ruoyi.gpt.model.domain.PromptManage;
import lombok.Data;

import java.util.List;

@Data
public class PromptGroupHomeListVo {
    private String name;
    private String icon;
    private List<PromptManage> promptManageList;
}
