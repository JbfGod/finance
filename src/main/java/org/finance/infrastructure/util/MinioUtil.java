package org.finance.infrastructure.util;

import cn.hutool.core.util.StrUtil;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.finance.infrastructure.exception.HxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * minio 工具类
 * Minio是什么？https://docs.min.io/cn/minio-quickstart-guide.html
 */
public class MinioUtil {

    @Value("${minio.url:http://127.0.0.1:9000}")
    private String url;
    @Value("${minio.accessKey:jiangbangfa}")
    private String accessKey;
    @Value("${minio.secretKey:jiangbangfa}")
    private String secretKey;
    @Value("${minio.bucketName:jxzl}")
    private String bucketName;
    
    /**
     * minio 客户端
     */
    private final MinioClient client;

    public MinioUtil() {
        client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 上传文件至minio服务器
     * @param file 待上传文件
     * @param fileDir 文件保存目录
     * @return minio 中的相对文件路径
     */
    public String upload(MultipartFile file, String customName, String fileDir) {
        String originalName = file.getOriginalFilename();
        if (StringUtils.isBlank(customName)) {
            originalName = generateNewName(customName + getSuffix(originalName), false, fileDir);
        } else {
            originalName = generateNewName(originalName, true, fileDir);
        }
        String objectName = originalName;
        try {
            String newBucket = createBucketIfNotExists(bucketName);
            client.putObject(
                PutObjectArgs.builder()
                    .bucket(newBucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new HxException("Minio服务异常");
        }
    }

    /**
     * 文件名根据时间戳自动生成
     * @param file
     * @param fileDir
     * @return
     */
    public String upload(MultipartFile file, String fileDir) {
        return upload(file, null, fileDir);
    }
    /**
     * 上传文件至minio服务器
     * 文件上传成功,返回文件访问路径
     * 文件上传失败,返回 null
     *
     * @param file    待上传文件
     * @param fileDir 文件保存目录
     * @return oss 中的相对文件路径
     */
    public String upload(File file, String fileDir) {
        String objectName = generateNewName(file.getName(), true, fileDir);
        try {
            String newBucket = createBucketIfNotExists(bucketName);
            client.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(newBucket)
                            .object(objectName)
                            .filename(file.getAbsolutePath())
                            .build()
            );
            return objectName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void copy(String objectName, OutputStream os) throws Exception {
        try (InputStream in = client.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build())) {
            FileCopyUtils.copy(in, os);
        }
    }

    private String getSuffix(String filename){
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        int index = filename.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return filename.substring(index);
    }

    private String generateNewName(String originalName, boolean needTimeMillis, String dirName){
        if (StrUtil.isBlank(originalName)) {
            originalName = "";
        }
        if (needTimeMillis) {
            originalName += ("_" + System.currentTimeMillis());
        }
        originalName += getSuffix(originalName);
        if (StrUtil.isNotBlank(dirName)) {
            return addDirSuffix(dirName) + originalName;
        }
        return originalName;
    }

    /**
     * 如果存储桶不存在则创建一个
     * @param customBucket 存储桶
     * @return customBucket is empty则返回bucketName，否则返回customBucket
     */
    private String createBucketIfNotExists(String customBucket) throws Exception {
        String newBucket = customBucket;
        if (StrUtil.isBlank(customBucket)) {
            newBucket = bucketName;
        }
        // 判断存储桶是否存在，不存在则创建存储桶
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(newBucket).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(newBucket).build());
        }
        return newBucket;
    }

    private String addDirSuffix(String fileDir){
        if ("/".equals(fileDir)) {
            return "";
        }
        String dirStr = Optional.ofNullable(fileDir).orElse("");
        if (dirStr.startsWith("/")) {
            dirStr = dirStr.substring(1);
        }
        if (!dirStr.endsWith("/")) {
            dirStr += "/";
        }
        return dirStr;
    }

}
