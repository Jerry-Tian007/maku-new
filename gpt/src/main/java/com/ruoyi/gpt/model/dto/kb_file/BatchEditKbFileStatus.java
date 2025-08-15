package com.ruoyi.gpt.model.dto.kb_file;

import lombok.Data;

import java.util.List;

@Data
public class BatchEditKbFileStatus {

    private List<Long> fileIds;

    private String status;

}
