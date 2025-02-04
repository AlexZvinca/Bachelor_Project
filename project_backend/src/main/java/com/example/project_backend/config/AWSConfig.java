package com.example.project_backend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${S3_ENDPOINT_URL}")
    private String s3EndpointUrl;

    @Value("${S3_ACCESS_KEY}")
    private String s3AccessKey;

    @Value("${S3_SECRET_KEY}")
    private String s3SecretKey;

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3EndpointUrl, "auto"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
