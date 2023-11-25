package com.example.trainermicroservice.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerMonthlySummary {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private String trainerStatus;
    private Map<Integer, Map<String, Integer>> yearlyTrainingSummary;

    // Getters y setters para cada propiedad
    // ...
}