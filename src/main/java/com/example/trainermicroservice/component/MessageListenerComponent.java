package com.example.trainermicroservice.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.trainermicroservice.model.TrainerWorkloadRequest;
import com.example.trainermicroservice.service.implementation.TrainerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class MessageListenerComponent {

    @Autowired
    private TrainerServiceImpl trainerServiceImpl;

    @JmsListener(destination = "add.workload")
    public void receiveMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println("Received: ".concat(message));
        try {
            TrainerWorkloadRequest request = mapper.readValue(message, TrainerWorkloadRequest.class);
            System.out.println(request.getTrainerFirstName());
            trainerServiceImpl.addWorkload(request);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("Error processing message", e);
        }
    }
}