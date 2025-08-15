package com.ruoyi.gpt.model.enums;

/**
 * 文件后缀枚举
 *
 * @author tiankk
 */
public enum FileSuffixEnum {
    PPT("ppt"),
    PPTX("pptx"),
    PDF("pdf"),
    TXT("txt"),
    XLSX("xlsx"),
    DOC("doc"),
    DOCX("docx");

    private final String name;

    FileSuffixEnum(String name) {
        this.name = name;
    }

    /**
     * 获取Code
     *
     * @return 状态码
     */
    public String getName() {
        return name;
    }
}
