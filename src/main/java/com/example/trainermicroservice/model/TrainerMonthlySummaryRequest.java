package com.example.trainermicroservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerMonthlySummaryRequest {
    @NotNull
    @NotEmpty
    private String trainerUsername;
}