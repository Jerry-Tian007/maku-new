package com.ruoyi.gpt.model.dto.kb_q_a_record;

import lombok.Data;

/**
 * @author Liujr
 */
@Data
public class KbQARecordStateDto {
    private String yes_count;
    private String no_count;
    private String null_count;
    private String unsolved_count;
    private String solved_count;

}
