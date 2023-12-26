package com.example.trainermicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainermicroservice.model.TrainerMonthlySummary;
import com.example.trainermicroservice.model.TrainerMonthlySummaryRequest;
import com.example.trainermicroservice.model.TrainerWorkload;
import com.example.trainermicroservice.model.TrainerWorkloadRequest;
import com.example.trainermicroservice.service.implementation.TrainerServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainerServiceImpl trainerServiceImpl;

    @GetMapping("/workload")
    public ResponseEntity<TrainerMonthlySummary> handleTrainerWorkload(@RequestParam String trainerUsername) {
        TrainerMonthlySummaryRequest trainerMonthlySummaryRequest = new TrainerMonthlySummaryRequest(trainerUsername);
        TrainerMonthlySummary response = trainerServiceImpl.calculateMonthlySummary(trainerMonthlySummaryRequest);

        HttpStatus responseStatus = response == null ? HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(response, responseStatus);
    }

    @PutMapping("/modify")
    @ResponseBody
    public ResponseEntity<TrainerWorkload> addOrRemoveWorkload(@Valid @RequestBody TrainerWorkloadRequest request) {
        TrainerWorkload workload = trainerServiceImpl.addWorkload(request);

        HttpStatus responseStatus = workload == null ? HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(workload, responseStatus);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAll() {
        return trainerServiceImpl.deleteEverything() ? ResponseEntity.ok().body(null)
                : ResponseEntity.badRequest().body(null);
    }
}