package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.TimeSlot;
import com.coinquyteam.shift.Repository.IHouseTaskRepository;
import com.coinquyteam.shift.Repository.ITimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseTaskService
{
    @Autowired private IHouseTaskRepository houseTaskRepository;
    @Autowired private ITimeSlotRepository timeSlotRepository;

    public void createTask(HouseTask houseTask) throws Exception
    {
        if (houseTask == null) {
            throw new IllegalArgumentException("HouseTask cannot be null");
        }

        System.out.println(houseTask.getTaskCategory()+" "+houseTask.getTimeSlot());
        TimeSlot ts= new TimeSlot(houseTask.getTimeSlot().getStart(),  houseTask.getTimeSlot().getStart().plusDays(1));
        HouseTask nhs= new HouseTask(houseTask.getTaskCategory(), houseTask.getDescription(), ts, houseTask.getHouseId());
        try
        {
            timeSlotRepository.insert(ts);
            houseTaskRepository.insert(nhs);
        }
        catch (Exception e)
        {
            throw new Exception("Failed to create task: " + e.getMessage(), e);
        }
    }

    public List<HouseTask> getTasksByHouseId(String houseId)
    {
        return houseTaskRepository.findAllByHouseId(houseId);
    }
}
