package com.ruoyi.gpt.service.impl;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.gpt.core.cos.CosClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    @Service
    public class CosImageServiceImpl {
        @Value("${tencent.cos.bucketName}")
        private String bucketName;

        @Value("${tencent.cos.baseUrl}")
        private String baseUrl;

        @Autowired
        private CosClientConfig cosClientConfig;

        // 图片扩展名常量
        public static final Set<String> IMAGE_EXTENSIONS = Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList(
                        ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".tiff", ".svg", ".ico"
                ))
        );
        public static final String YAML_TYPE =  ".yaml";

        // 解析原始签名 URL，提取 bucket、region、prefix
        private String[] parseCosUrl(String signedUrl) throws Exception {
            URL url = new URL(signedUrl);
            String host = url.getHost();

            Pattern pattern = Pattern.compile("^(.+?)\\.cos\\.(.+?)\\.myqcloud\\.com$");
            Matcher matcher = pattern.matcher(host);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid COS URL: " + host);
            }

            String bucket = matcher.group(1);
            String region = matcher.group(2);

            String path = url.getPath();
            int lastSlash = path.lastIndexOf('/');
            String prefix = lastSlash == -1 ? "" : path.substring(0, lastSlash + 1);

            return new String[]{bucket, region, prefix};
        }

        // 列出目录下所有图片文件，并生成签名 URL
        public Map<String, List<String>> listAndSignImageUrls(String signedUrl) {
            Map<String, List<String>> result = new HashMap<>();
            List<String> yamlUrls = new ArrayList<>();

            List<String> signedImageUrls = new ArrayList<>();
            if (StringUtils.isNotBlank(signedUrl)) {
                try {

                    String[] info = parseCosUrl(signedUrl);
                    String bucket = info[0];
                    String region = info[1];
                    String prefix = info[2];

                    // 列出目录下所有文件
                    ListObjectsRequest listReq = new ListObjectsRequest();
                    listReq.setBucketName(bucket);
                    listReq.setPrefix(prefix);
                    listReq.setDelimiter(null); // 获取所有层级文件

                    ObjectListing listing;
                    List<COSObjectSummary> objectSummaries = new ArrayList<>();
                    do {
                        listing = cosClientConfig.cosClient().listObjects(listReq);
                        objectSummaries.addAll(listing.getObjectSummaries());
                        listReq.setMarker(listing.getNextMarker());
                    } while (listing.isTruncated());


                    // 筛选图片文件并生成签名 URL（有效期 1 小时）
                    for (COSObjectSummary summary : objectSummaries) {
                        String key = summary.getKey();

                        if (key.endsWith("/")) continue; // 跳过目录
                        String lowerKey = key.toLowerCase();
                        boolean isImage = IMAGE_EXTENSIONS.stream().anyMatch(lowerKey::endsWith);

                        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key, HttpMethodName.GET);
                        // 设置签名有效期为 1 小时
                        Date expirationDate = new Date(System.currentTimeMillis() + 36500L * 24 * 60 * 60 * 1000); // 链接有效期为1小时
                        req.setExpiration(expirationDate);

                        if (isImage) {

                            URL presignedUrl = cosClientConfig.cosClient().generatePresignedUrl(req);
                            signedImageUrls.add(presignedUrl.toString());
                        } else if (lowerKey.endsWith(YAML_TYPE)){
                            URL presignedUrl = cosClientConfig.cosClient().generatePresignedUrl(req);
                            yamlUrls.add(presignedUrl.toString());
                        }
                    }
                } catch (Exception e) {
                    // MD not a valid format.
                    throw new RuntimeException(e);
                }
            }
            result.put("YAML",yamlUrls);
            result.put("IMAGE",signedImageUrls);
            return result;
        }
    }
