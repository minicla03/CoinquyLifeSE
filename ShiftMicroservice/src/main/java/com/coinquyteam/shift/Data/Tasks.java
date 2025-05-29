package com.coinquyteam.shift.Data;

public enum Tasks
{
    CLEANING("Cleaning"),
    MAINTENANCE("Maintenance"),
    REPAIR("Repair");

    private final String taskName;

    Tasks(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }
}
