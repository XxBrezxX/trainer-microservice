package com.example.trainermicroservice.service.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.trainermicroservice.model.Document.Trainer;

public interface TrainerDao extends MongoRepository<Trainer, String>{
    Trainer findByUsername(String username);
}
