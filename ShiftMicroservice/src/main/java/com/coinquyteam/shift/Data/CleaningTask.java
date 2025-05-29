package com.coinquyteam.shift.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("CleaningTasks")
public class CleaningTask
{
    @Id @Field("idTask") private String id;
    @Field("task") private Tasks task;
    @Field("TimeSlot") private TimeSlot timeSlot;

    public CleaningTask() { }

    public CleaningTask(Tasks task, TimeSlot timeSlot)
    {
        this.task = task;
        this.timeSlot = timeSlot;
    }

    public Tasks getTask()
    {
        return this.task;
    }

    public void setTask(Tasks task)
    {
        this.task = task;
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