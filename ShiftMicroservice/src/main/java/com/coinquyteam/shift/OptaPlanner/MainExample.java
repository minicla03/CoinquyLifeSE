package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.*;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.time.LocalDateTime;
import java.util.*;

public class MainExample
{
    public static void main(String[] args)
    {
        SolverFactory<CleaningSchedule> solverFactory =
                SolverFactory.createFromXmlResource("solverConfig.xml");
        Solver<CleaningSchedule> solver = solverFactory.buildSolver();

        Roommate alice = new Roommate("Alice", List.of(
                new TimeSlot(LocalDateTime.of(2025, 6, 1, 10, 0),
                        LocalDateTime.of(2025, 6, 1, 12, 0))));
        Roommate bob = new Roommate("Bob", List.of());
        Roommate carol = new Roommate("Carol", List.of());

        List<Roommate> roommates = Arrays.asList(alice, bob, carol);

        List<CleaningTask> tasks = new ArrayList<>();
        for (int i = 1; i <= 7; i++)
        {
            tasks.add(new CleaningTask(Tasks.CLEANING, new TimeSlot(LocalDateTime.of(2025, 6, i, 18, 0), LocalDateTime.of(2025, 6, i, 19, 0))));
        }

        List<CleaningAssignment> assignments = new ArrayList<>();
        int id = 1;
        for (CleaningTask task : tasks)
        {
            assignments.add(new CleaningAssignment(id++, task));
        }

        assignments.get(0).setAssignedRoommate(alice); // 1 giugno
        assignments.get(1).setAssignedRoommate(bob);   // 2 giugno

        SwapRequest swapRequest = new SwapRequest(assignments.get(0), assignments.get(1),true);

        CleaningSchedule schedule = new CleaningSchedule();
        schedule.setRoommateList(roommates);
        schedule.setTaskList(tasks);
        schedule.setAssignmentList(assignments);
        schedule.setSwapRequestList(List.of(swapRequest));

        System.out.println("📋 Stato iniziale delle assegnazioni:");
        schedule.getAssignmentList().stream()
                .sorted(Comparator.comparing(a -> a.getTask().getTimeSlot().getStart()))
                .forEach(a -> System.out.printf("🔹 %s - %s → %s\n",
                        a.getTask().getTimeSlot().getStart().toLocalDate(),
                        a.getTask().getTask(),
                        a.getAssignedRoommate() != null ? a.getAssignedRoommate().getName() : "❌ Nessuno"));

        CleaningSchedule solvedSchedule = solver.solve(schedule);

        System.out.println("\n📋 Risultato finale dei turni di pulizia:");
        solvedSchedule.getAssignmentList().stream()
                .sorted(Comparator.comparing(a -> a.getTask().getTimeSlot().getStart()))
                .forEach(a -> System.out.printf("🧹 %s - %s → %s\n",
                        a.getTask().getTimeSlot().getStart().toLocalDate(),
                        a.getTask().getTask(),
                        a.getAssignedRoommate() != null ? a.getAssignedRoommate().getName() : "❌ Nessuno"));

        System.out.println("\n🔢 Score finale: " + solvedSchedule.getScore());
    }
}
