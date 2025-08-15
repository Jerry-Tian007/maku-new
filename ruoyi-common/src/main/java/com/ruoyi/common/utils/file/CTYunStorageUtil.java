package com.ruoyi.common.utils.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 天翼云对象存储帮助类
 *
 * @author tiankk
 */
public class CTYunStorageUtil {

    //访问对象
    AmazonS3 s3 = null;

    public CTYunStorageUtil(String accessKey, String secretKey, String region, String endPoint) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");
        clientConfiguration.setProtocol(Protocol.HTTP);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                endPoint, region);
        s3 = AmazonS3ClientBuilder.standard()
                //客户端设置
                .withClientConfiguration(clientConfiguration)
                //凭证设置
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                //endpoint设置
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    /**
     * 上传文件到对象存储中
     *
     * @param fileUrl    本地文件路径
     * @param key        文件唯一标识key
     * @param bucketName 桶名称
     * @return
     */
    public boolean uploadFile(String fileUrl, String key, String bucketName) {
        try {
            PutObjectResult putObjectResult = s3.putObject(bucketName, key, new File(fileUrl));
            System.out.format("Etag: %s, VersionId: %s\n", putObjectResult.getETag(),
                    putObjectResult.getVersionId());
            return true;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        return false;
    }

    /**
     * 上传文件到对象存储中
     *
     * @param inputStream 文件流
     * @param key         文件唯一标识key
     * @param fileName    文件名称
     * @param bucketName  桶名称
     * @return
     */
    public boolean uploadFile(InputStream inputStream, String key, String fileName, String bucketName) {
        try {
            String filePath = System.getProperty("user.dir") + "/ctyun-temp/";
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(filePath + fileName);
            copyInputStreamToFile(inputStream, file);
            PutObjectResult putObjectResult = s3.putObject(bucketName, key, file);
            System.out.format("Etag: %s, VersionId: %s\n", putObjectResult.getETag(),
                    putObjectResult.getVersionId());
            file.delete();
            return true;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        return false;
    }

    /**
     * 获取下载链接，根据key设置过去时间
     *
     * @param key        文件唯一标识key
     * @param extTime    过期时间
     * @param bucketName 桶名称
     * @return
     */
    public URL getDownLoadFileUrl(String key, Date extTime, String bucketName) {
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(extTime);
            return s3.generatePresignedUrl(request);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param key        文件唯一标识key
     * @param bucketName 桶名称
     * @return
     */
    public Boolean deleteObject(String key, String bucketName) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
            s3.deleteObject(deleteObjectRequest);
            return true;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        return false;
    }

    /**
     * 查询桶中的文件列表
     *
     * @param bucketName 桶名称
     * @return
     */
    public List<S3ObjectSummary> listObjects(String bucketName) {
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            listObjectsRequest.setBucketName(bucketName);
            ObjectListing result = s3.listObjects(listObjectsRequest);
            return result.getObjectSummaries();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        return null;
    }

    // InputStream -> File
    private void copyInputStreamToFile(InputStream inputStream, File file) {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // commons-io
            //IOUtils.copy(inputStream, outputStream);

        } catch (Exception e) {

        }

    }

}
