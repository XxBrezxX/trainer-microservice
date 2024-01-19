package com.example.trainermicroservice.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;



@Configuration
public class DynamoDbConfig {
    
    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${aws.sqs.queue.url}") // Reemplazar por endpoint a bd
    private String url;
    
    @Bean
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder()
        .region(Region.of(region))
        .endpointOverride(URI.create(url))
        .build();
    }
}
