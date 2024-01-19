package com.example.trainermicroservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {
    
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public SqsClient createSqsClient() {
        return SqsClient.builder()
                .region(Region.of(region))
                .build();
    }
}
