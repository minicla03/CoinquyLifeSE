package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.*;
import com.coinquyteam.shift.OptaPlanner.ScheduleSolution;
import com.coinquyteam.shift.OptaPlanner.CleaningSchedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {
        try {
            // Creazione di alcuni slot temporali
            TimeSlot slot1 = new TimeSlot(LocalDateTime.of(2025, 6, 10, 10, 0), LocalDateTime.of(2025, 6, 10, 12, 0));
            TimeSlot slot2 = new TimeSlot(LocalDateTime.of(2025, 6, 11, 10, 0), LocalDateTime.of(2025, 6, 11, 12, 0));
            TimeSlot slot3 = new TimeSlot(LocalDateTime.of(2025, 6, 12, 10, 0), LocalDateTime.of(2025, 6, 12, 12, 0));

            HouseTask task1 = new HouseTask(TaskCategory.CLEANING, "Clean the kitchen", slot1,  "house1");
            HouseTask task2 = new HouseTask(TaskCategory.REPAIR, "Clean the bathroom", slot2,  "house1");
            HouseTask task3 = new HouseTask(TaskCategory.REPAIR, "Clean the living room", slot3,  "house1");
            List<HouseTask> tasks = Arrays.asList(task1, task2, task3);

            // Creazione dei coinquilini
            Roommate r1 = new Roommate("alice", "house1", List.of(slot3)); // indisponibile nel giorno 12
            Roommate r2 = new Roommate("bob", "house1", new ArrayList<>()); // sempre disponibile

            List<Roommate> roommates = Arrays.asList(r1, r2);

            // Creazione della soluzione
            ScheduleSolution scheduleSolution = new ScheduleSolution();
            CleaningSchedule result = scheduleSolution.solve(UUID.randomUUID(), roommates, tasks);

            // Stampa risultati
            System.out.println("Assegnazioni generate:");
            result.getAssignmentList().forEach(a -> {
                System.out.printf("Task: %s - Periodo: %s - Assegnato a: %s%n",
                        a.getTask().getDescription(),
                        a.getTask().getTimeSlot().toString(),
                        a.getAssignedRoommate() != null ? a.getAssignedRoommate().getUsernameRoommate() : "Nessuno");
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
