package com.example.trainermicroservice.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.trainermicroservice.model.TrainerMonthlySummary;
import com.example.trainermicroservice.model.TrainerMonthlySummaryRequest;
import com.example.trainermicroservice.model.TrainerWorkload;
import com.example.trainermicroservice.model.TrainerWorkloadRequest;
import com.example.trainermicroservice.model.Document.MonthDocument;
import com.example.trainermicroservice.model.Document.Trainer;
import com.example.trainermicroservice.model.Document.YearDocument;
import com.example.trainermicroservice.model.TrainerWorkloadRequest.ActionType;
import com.example.trainermicroservice.service.DAO.TrainerDao;
import com.example.trainermicroservice.service.interfaces.TrainerService;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerDao trainerDao;

    private Map<String, List<TrainerWorkload>> data = new HashMap<>();

    @Override
    public TrainerMonthlySummary calculateMonthlySummary(TrainerMonthlySummaryRequest request) {
        List<TrainerWorkload> workloads = data.get(request.getTrainerUsername());
        if (workloads == null)
            return null;

        String trainerFirstName = workloads.get(0).getTrainerFirstName();
        String trainerUsername = workloads.get(0).getTrainerUsername();
        String trainerLastName = workloads.get(0).getTrainerLastName();
        String trainerStatus = workloads.get(0).isActive() ? "active" : "inactive";

        // Inicializa el mapa anidado para almacenar los resúmenes de entrenamiento por
        // año y mes
        Map<Integer, Map<String, Integer>> yearlyTrainingSummary = new HashMap<>();

        for (TrainerWorkload workload : workloads) {
            int year = workload.getTrainingDate().getYear();
            String month = workload.getTrainingDate().getMonth().name();
            int duration = workload.getTrainingDuration();

            // Si el año aún no está en el mapa, lo agrega
            yearlyTrainingSummary.putIfAbsent(year, new HashMap<>());

            // Si el mes aún no está en el mapa para este año, lo agrega con una duración
            // inicial de 0
            yearlyTrainingSummary.get(year).putIfAbsent(month, 0);

            // Suma la duración de la carga de trabajo actual a la duración existente para
            // este mes y año
            int updatedDuration = yearlyTrainingSummary.get(year).get(month) + duration;
            yearlyTrainingSummary.get(year).put(month, updatedDuration);
        }

        // Crea el objeto TrainerMonthlySummary
        TrainerMonthlySummary summary = new TrainerMonthlySummary();
        summary.setTrainerUsername(trainerUsername);
        summary.setTrainerFirstName(trainerFirstName);
        summary.setTrainerLastName(trainerLastName);
        summary.setTrainerStatus(trainerStatus);

        // Configura la estructura de resumen anidada
        summary.setYearlyTrainingSummary(yearlyTrainingSummary);

        return summary;
    }

    @Override
    public TrainerWorkload addWorkload(TrainerWorkloadRequest request) {
        List<TrainerWorkload> workloads = data.get(request.getTrainerUsername());
        TrainerWorkload trainerWorkload = new TrainerWorkload();

        trainerWorkload.setActive(request.isActive());
        trainerWorkload.setTrainerFirstName(request.getTrainerFirstName());
        trainerWorkload.setTrainerLastName(request.getTrainerLastName());
        trainerWorkload.setTrainerUsername(request.getTrainerUsername());
        trainerWorkload.setTrainingDate(request.getTrainingDate());
        trainerWorkload.setTrainingDuration(request.getTrainingDuration());

        if (request.getActionType() == ActionType.ADD) {
            if (workloads == null) {
                workloads = new ArrayList<>();
                data.put(request.getTrainerUsername(), workloads);
            }

            Trainer trainer = trainerDao.findByUsername(request.getTrainerUsername());
            if (trainer == null) {
                trainer = new Trainer();
                trainer.setUsername(request.getTrainerUsername());
                trainer.setFirstName(request.getTrainerFirstName());
                trainer.setLastName(request.getTrainerLastName());
                trainer.setStatus(request.isActive());

                Integer duration = request.getTrainingDuration();

                MonthDocument monthDocument = new MonthDocument();
                monthDocument.setDuration(duration);
                monthDocument.setMonth(request.getTrainingDate().getMonthValue());

                Set<MonthDocument> months = new HashSet<>();
                months.add(monthDocument);

                YearDocument yearDocument = new YearDocument();
                yearDocument.setMonths(months);
                yearDocument.setYear(request.getTrainingDate().getYear());

                Set<YearDocument> years = new HashSet<>();
                years.add(yearDocument);

                trainer.setYearsList(years);

                trainerDao.insert(trainer);
            } else {
                Optional<YearDocument> yearDoc = trainer.getYearsList().stream()
                        .filter(y -> y.getYear().equals(request.getTrainingDate().getYear()))
                        .findFirst();
                if (yearDoc.isPresent()) {
                    Optional<MonthDocument> monthDoc = yearDoc.get().getMonths().stream()
                            .filter(m -> m.getMonth().equals(request.getTrainingDate().getMonthValue()))
                            .findFirst();
                    if (monthDoc.isPresent()) {
                        monthDoc.get().setDuration(monthDoc.get().getDuration() + request.getTrainingDuration());
                    } else {
                        Integer duration = request.getTrainingDuration();

                        MonthDocument monthDocument = new MonthDocument();
                        monthDocument.setDuration(duration);
                        monthDocument.setMonth(request.getTrainingDate().getMonthValue());

                        yearDoc.get().getMonths().add(monthDocument);
                    }
                } else {
                    Set<MonthDocument> months = new HashSet<>();
                    months.add(new MonthDocument(request.getTrainingDate().getMonthValue(),
                            request.getTrainingDuration()));

                    YearDocument yearDocument = new YearDocument();
                    yearDocument.setMonths(months);
                    yearDocument.setYear(request.getTrainingDate().getYear());

                    trainer.getYearsList().add(yearDocument);
                }
                trainerDao.save(trainer);
            }

            workloads.add(trainerWorkload);
        } else {
            if (workloads == null)
                return trainerWorkload;
            workloads.removeIf(
                    otherWorkload -> {
                        return otherWorkload.getTrainingDate().isEqual(trainerWorkload.getTrainingDate())
                                && otherWorkload.getTrainingDuration() == trainerWorkload.getTrainingDuration();
                    });
        }
        System.out.println("$$$$$$$$$:".concat(workloads.size() + ""));
        return trainerWorkload;
    }

}
