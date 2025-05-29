package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import com.coinquyteam.shift.Data.SwapRequest;
import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import com.coinquyteam.shift.OptaPlanner.CleaningSchedule;
import com.coinquyteam.shift.OptaPlanner.ScheduleSolution;
import com.coinquyteam.shift.Repository.IRoommateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CalendarService
{
    @Autowired private CleaningSchedule cleaningSchedule;
    @Autowired private IRoommateRepository roommateRepository;
    @Autowired private HouseTaskService houseTaskService;
    @Autowired private SwapService swapService;

    public CleaningSchedule getSchedule(String houseId) throws ExecutionException, InterruptedException
    {
        ScheduleSolution solution = new ScheduleSolution();
        List<Roommate> roommates = roommateRepository.findAllByHouseId(houseId);
        List<HouseTask> tasks = houseTaskService.getTasksByHouseId(houseId);
        List<SwapRequest> swapRequests = swapService.getSwapsByHouseId(houseId);

        if (roommates.isEmpty() || tasks.isEmpty())
        {
            throw new IllegalArgumentException("House must have at least one roommate and one task.");
        }
        if (swapRequests == null)
        {
            swapRequests = List.of(); // Ensure swapRequests is not null
        }
        return solution.solve(roommates, tasks, swapRequests);
    }
}
