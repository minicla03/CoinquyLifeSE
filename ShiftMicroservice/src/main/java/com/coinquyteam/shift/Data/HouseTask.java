package com.coinquyteam.shift.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("CleaningTasks")
public class HouseTask
{
    @Id @Field("idTask") private String idTask;
    @Field("task") private TaskCategory task;
    @Field("houseId") private String houseId;
    @Field("description") private String description;
    @Field("TimeSlot") private TimeSlot timeSlot;
    @Field("isDone") private boolean isDone = false;

    public HouseTask() { }

    public HouseTask(TaskCategory task, String description, TimeSlot timeSlot, String houseId)
    {
        this.task = task;
        this.description = description;
        this.timeSlot = timeSlot;
        this.houseId = houseId;
        this.isDone = false;
    }

    public String getIdTask()
    {
        return this.idTask;
    }

    public void setIdTask(String idTask)
    {
        this.idTask = idTask;
    }

    public TaskCategory getTaskCategory()
    {
        return this.task;
    }

    public void setTaskCategory(TaskCategory task)
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

    public boolean isDone() { return isDone; }
    public void setDone(boolean done) {isDone = done; }
}