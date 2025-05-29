package com.coinquyteam.shift.Service;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Repository.IHouseTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseTaskService
{
    @Autowired private IHouseTaskRepository houseTaskRepository;

    public void createTask(HouseTask houseTask, String houseId) throws Exception
    {
        if (houseTask == null) {
            throw new IllegalArgumentException("HouseTask cannot be null");
        }

        if (houseId == null || houseId.isEmpty()) {
            throw new IllegalArgumentException("House ID is required");
        }

        try
        {
            houseTask.setHouseId(houseId);
            houseTaskRepository.insert(houseTask);
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
