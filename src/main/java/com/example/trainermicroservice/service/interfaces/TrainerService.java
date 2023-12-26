package com.example.trainermicroservice.service.interfaces;

import com.example.trainermicroservice.model.TrainerWorkloadRequest;
import com.example.trainermicroservice.model.TrainerMonthlySummary;
import com.example.trainermicroservice.model.TrainerMonthlySummaryRequest;
import com.example.trainermicroservice.model.TrainerWorkload;

public interface TrainerService {
    TrainerMonthlySummary calculateMonthlySummary(TrainerMonthlySummaryRequest trainerMonthlySummaryRequest);
    TrainerWorkload addWorkload(TrainerWorkloadRequest request);
    Boolean deleteEverything();
}