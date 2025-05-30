package com.coinquyteam.shift.Data;

public enum TaskCategory
{
    CLEANING("Cleaning", 10, -5),
    MAINTENANCE("Maintenance", 15, -10),
    REPAIR("Repair", 20, -15);

    private final String taskName;
    private final int points;
    private final int penalityPoints;

    TaskCategory(String taskName, int points, int penalityPoints)
    {
        this.taskName = taskName;
        this.points = points;
        this.penalityPoints = penalityPoints;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getPoints() {
        return points;
    }

    public int getPenalityPoints() {
        return penalityPoints;
    }
}
