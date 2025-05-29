package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.CleaningTask;
import com.coinquyteam.shift.Data.SwapRequest;
import com.coinquyteam.shift.Data.TimeSlot;
import com.coinquyteam.shift.Repository.ICleaningTaskRepository;
import com.coinquyteam.shift.Repository.ISwapRequestRepository;
import com.coinquyteam.shift.Repository.ITimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService
{
    @Autowired private ITimeSlotRepository timeSlotRepository;
    @Autowired private ICleaningTaskRepository cleaningTaskRepository;
    @Autowired private ISwapRequestRepository swapRequestRepository;

    public void addUnavaibleShift(TimeSlot timeSlot)
    {
        // Logic to add an unavailable shift
        timeSlotRepository.save(timeSlot);
    }

    public void swapShift(SwapRequest swapRequest)
    {
        // Logic to handle a shift swap request
        swapRequestRepository.save(swapRequest);
    }

    public void addCleaningTask(CleaningTask cleaningTask)
    {
        // Logic to add a cleaning task
        cleaningTaskRepository.save(cleaningTask);
    }

    public void removeCleaningTask(String taskId)
    {
        // Logic to remove a cleaning task by ID
        cleaningTaskRepository.deleteById(taskId);
    }

    public void removeSwapRequest(String requestId)
    {
        // Logic to remove a swap request by ID
        swapRequestRepository.deleteById(requestId);
    }

    public void removeUnavaibleShift(String shiftId)
    {
        // Logic to remove an unavailable shift by ID
        timeSlotRepository.deleteById(shiftId);
    }

    public void updateCleaningTask(CleaningTask cleaningTask)
    {
        // Logic to update a cleaning task
        cleaningTaskRepository.save(cleaningTask);
    }

    public void updateSwapRequest(SwapRequest swapRequest)
    {
        // Logic to update a swap request
        swapRequestRepository.save(swapRequest);
    }

    public void updateUnavaibleShift(TimeSlot timeSlot)
    {
        // Logic to update an unavailable shift
        timeSlotRepository.save(timeSlot);
    }

    public List<CleaningTask> getAllCleaningTasks()
    {
        // Logic to retrieve all cleaning tasks
        return cleaningTaskRepository.findAll();
    }


}
