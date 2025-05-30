package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import com.coinquyteam.shift.Data.SwapRequest;
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
    @Autowired private IRoommateRepository roommateRepository;
    @Autowired private HouseTaskService houseTaskService;
    @Autowired private SwapService swapService;
    private CleaningSchedule cleaningSchedule;

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
        cleaningSchedule= solution.solve(roommates, tasks, swapRequests);
        return cleaningSchedule;
    }

    public boolean modifyScheduleAfterSwap(SwapRequest swapRequest) throws ExecutionException, InterruptedException
    {
        if (cleaningSchedule == null)
        {
            throw new IllegalStateException("Cleaning schedule has not been initialized. Call getSchedule() first.");
        }

        Roommate roommateA= swapRequest.getAssignmentA().getAssignedRoommate();
        Roommate roommateB= swapRequest.getAssignmentB().getAssignedRoommate();

        try {
            cleaningSchedule.getAssignmentList().stream()
                    .filter(assignment -> assignment.getAssignedRoommate().equals(roommateA))
                    .forEach(assignment -> assignment.setAssignedRoommate(roommateB));
            cleaningSchedule.getAssignmentList().stream()
                    .filter(assignment -> assignment.getAssignedRoommate().equals(roommateB))
                    .forEach(assignment -> assignment.setAssignedRoommate(roommateA));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
