package com.coinquyteam.shift.Data;

public class CleaningTask
{
    private Tasks task;
    private TimeSlot timeSlot;

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