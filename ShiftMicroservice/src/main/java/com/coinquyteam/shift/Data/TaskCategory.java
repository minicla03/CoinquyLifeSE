package com.coinquyteam.shift.Data;

public enum TaskCategory
{
    CLEANING("Cleaning", 10),
    MAINTENANCE("Maintenance", 15),
    REPAIR("Repair", 20);

    private final String taskName;
    private final int points;

    TaskCategory(String taskName, int points)
    {
        this.taskName = taskName;
        this.points = points;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getPoints() {
        return points;
    }
}
