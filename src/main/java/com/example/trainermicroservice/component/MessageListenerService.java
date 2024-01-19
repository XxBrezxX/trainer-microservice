package com.example.trainermicroservice.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.trainermicroservice.model.TrainerWorkloadRequest;
import com.example.trainermicroservice.service.implementation.TrainerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Service
public class MessageListenerService {

    @Value("${aws.sqs.queue.url}")
    private String queueUrl;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private TrainerServiceImpl trainerServiceImpl;

    @Scheduled(fixedRate = 5000) // Escucha la cola cada 5 segundos
    public void listenToMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10) // Máximo número de mensajes a recibir
                .waitTimeSeconds(20) // Tiempo de espera por cada mensaje
                .visibilityTimeout(60) // El tiempo durante el cual el mensaje no estará visible para otros listeners
                .build();

        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);

        for (Message message : receiveMessageResponse.messages()) {
            // Procesa cada mensaje
            processMessage(message);

            // Borra mensaje después de ser procesado
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build());
        }
    }

    private void processMessage(Message message) {
        String data = message.body();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println("Received: ".concat(data));
        try {
            TrainerWorkloadRequest request = mapper.readValue(data, TrainerWorkloadRequest.class);
            System.out.println(request.getTrainerFirstName());
            trainerServiceImpl .addWorkload(request);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("Error processing message", e);
        }
    }
}