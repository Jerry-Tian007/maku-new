package com.ruoyi.gpt.propeties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "file-storage-purpose")
public class FileStoragePurposeProperty {

    private Map<String, String> blob;

    private Map<String, String> s3;

    private Map<String, String> local;

    private Map<String, String> ctYun;


}
