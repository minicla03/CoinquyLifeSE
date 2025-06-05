package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coinquyteam.shift.OptaPlanner.CleaningSchedule;
import com.coinquyteam.shift.OptaPlanner.ScheduleSolution;
import com.coinquyteam.shift.Repository.ICleaningAssignmentRepository;
import com.coinquyteam.shift.Repository.IRoommateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class CalendarService
{
    @Autowired
    private ICleaningAssignmentRepository cleaningAssignmentRepository;
    @Autowired
    private IRoommateRepository roommateRepository;
    @Autowired
    private HouseTaskService houseTaskService;
    @Autowired
    private RestTemplate restTemplate;

    private UUID problemId;

    public CleaningSchedule getSchedule(String houseId) throws ExecutionException, InterruptedException {
        ScheduleSolution solution = new ScheduleSolution();
        List<Roommate> roommates = roommateRepository.findAllByHouseId(houseId);
        List<HouseTask> tasks = houseTaskService.getTasksByHouseId(houseId).stream().filter(t-> !t.isDone()).toList();

        if (roommates.isEmpty() || tasks.isEmpty()) {
            throw new IllegalArgumentException("House must have at least one roommate and one task.");
        }

        problemId = UUID.randomUUID();
        CleaningSchedule cleaningSchedule=solution.solve(problemId,roommates, tasks);
        cleaningAssignmentRepository.insert(cleaningSchedule.getAssignmentList());
        return cleaningSchedule;
    }

    public void markTaskAsDone(String id)
    {
        cleaningAssignmentRepository.findById(id).ifPresentOrElse(
                cleaningAssignment -> {
                    if(!houseTaskService.taskDone(cleaningAssignment.getTask().getIdTask())) {
                        throw new IllegalArgumentException("Failed to mark task as done for ID: " + id);
                    }
                    cleaningAssignment.getTask().setDone(true);
                    cleaningAssignmentRepository.save(cleaningAssignment);
                },
                () -> {
                    throw new IllegalArgumentException("No cleaning assignment found with ID: " + id);
                }
        );

        houseTaskService.taskDone(id);
    }

    public String toRank(String token, CleaningAssignment cleaningAssignment) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Missing or invalid authorization token.");
        }
        if (cleaningAssignment == null || cleaningAssignment.getTask() == null) {
            throw new IllegalArgumentException("Cleaning assignment and task must not be null.");
        }

        String url = "http://localhost:8080/Rank/rest/rank/done";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        Map<String, CleaningAssignment> body = Map.of(
                "cleaningAssignment", cleaningAssignment);
        HttpEntity<Map<String, CleaningAssignment>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed with status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error calling /rank/done: " + e.getMessage(), e);
        }
    }

    public boolean planningExists(String pId, String houseId)
    {
        UUID uuid = UUID.fromString(pId);
        if (problemId.equals(uuid))
        {
            return cleaningAssignmentRepository.findAll().stream()
                    .filter(cleaningAssignment ->
                            cleaningAssignment.getProblemId().equals(uuid) &&
                                    cleaningAssignment.getTask().getHouseId().equals(houseId))
                    .anyMatch(cleaningAssignment -> !cleaningAssignment.getTask().isDone());
        }
        return false;
    }

    public List<CleaningAssignment> retriveCleaningAssignments(UUID problemId, String houseId)
    {
        return cleaningAssignmentRepository.findAll().stream()
                .filter(cleaningAssignment ->
                        cleaningAssignment.getProblemId().equals(problemId) &&
                                cleaningAssignment.getTask().getHouseId().equals(houseId))
                .toList();
    }

    public List<CleaningAssignment> getAllShifts(String houseId)
    {
        return cleaningAssignmentRepository.findAll().stream()
                .filter(cleaningAssignment -> cleaningAssignment.getTask().getHouseId().equals(houseId))
                .toList();
    }
}