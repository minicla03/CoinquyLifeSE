package com.coinquyteam.shift.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("CleaningTasks")
public class HouseTask
{
    @Id @Field("idTask") private String idTask;
    @Field("task") private Tasks task;
    @Field("houseId") private String houseId;
    @Field("description") private String description;
    @Field("TimeSlot") private TimeSlot timeSlot;

    public HouseTask() { }

    public HouseTask(Tasks task, String description, TimeSlot timeSlot)
    {
        this.task = task;
        this.description = description;
        this.timeSlot = timeSlot;
    }

    public String getIdTask()
    {
        return this.idTask;
    }

    public void setIdTask(String idTask)
    {
        this.idTask = idTask;
    }

    public Tasks getTask()
    {
        return this.task;
    }

    public void setTask(Tasks task)
    {
        this.task = task;
    }

    public String getHouseId()
    {
        return this.houseId;
    }

    public void setHouseId(String houseId)
    {
        this.houseId = houseId;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public TimeSlot getTimeSlot()
    {
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot)
    {
        this.timeSlot = timeSlot;
    }
}