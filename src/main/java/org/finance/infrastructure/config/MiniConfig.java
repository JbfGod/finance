package org.finance.infrastructure.config;

import io.minio.MinioClient;
import org.finance.infrastructure.constants.BucketName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangbangfa
 */
@Configuration
public class MiniConfig {

    @Value("${minio.url:http://127.0.0.1:9000}")
    private String url;
    @Value("${minio.accessKey:jiangbangfa}")
    private String accessKey;
    @Value("${minio.secretKey:jiangbangfa}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        initializationBucket(client);
        return client;
    }

    private void initializationBucket(MinioClient minioClient) throws Exception {
        for (BucketName bucket : BucketName.values()) {
            /*if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket.getName()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket.getName())
                        .build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucket.getName())
                        .config("PUBLIC")
                        .build());
            }*/
        }
    }
}
