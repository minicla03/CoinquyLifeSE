package com.coinquyteam.shift.Data;

public enum Tasks
{
    CLEANING("Cleaning"),
    MAINTENANCE("Maintenance");

    private final String taskName;

    Tasks(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }
}
