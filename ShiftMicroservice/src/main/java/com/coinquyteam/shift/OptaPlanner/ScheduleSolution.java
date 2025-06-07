package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ScheduleSolution
{
    private final SolverManager<CleaningSchedule, UUID> solverManager;

    public ScheduleSolution()
    {
        SolverFactory<CleaningSchedule> solverFactory = SolverFactory
                .createFromXmlResource("solverConfig.xml");
        this.solverManager = SolverManager.create(solverFactory);
    }

    public CleaningSchedule solve(UUID problemId,List<Roommate> roommates, List<HouseTask> tasks) throws ExecutionException, InterruptedException {

        List<CleaningAssignment> assignments = new ArrayList<>();
        IntStream.range(0, tasks.size()).forEach(i ->
                assignments.add(new CleaningAssignment(problemId,UUID.randomUUID().toString(), tasks.get(i)))
        );

        CleaningSchedule problem = new CleaningSchedule();
        problem.setRoommateList(roommates);
        problem.setTaskList(tasks);
        problem.setAssignmentList(assignments);

        return solverManager.solve(problemId, problem).getFinalBestSolution();
    }

    public SolverManager<CleaningSchedule, UUID> getSolverManager() {
        return solverManager;
    }
}

/*
La classe `ScheduleSolution` gestisce la risoluzione di un problema di pianificazione delle pulizie usando OptaPlanner.
Ecco cosa fa:

- Inizializza un `SolverManager` con una configurazione presa dal file `solverConfig.xml`.
- Nel metodo `solve`, crea una lista di assegnazioni (`CleaningAssignment`) per ogni compito (`HouseTask`), costruisce un oggetto `CleaningSchedule` con coinquilini, compiti e assegnazioni, e poi avvia la risoluzione del problema tramite OptaPlanner.
- Restituisce la soluzione migliore trovata.
- Espone un getter per il `SolverManager`.

Serve per automatizzare la distribuzione dei compiti di pulizia tra i coinquilini in modo ottimale.
 */
