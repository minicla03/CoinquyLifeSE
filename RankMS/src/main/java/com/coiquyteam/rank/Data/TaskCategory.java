package com.coiquyteam.rank.Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonCreator
    public static TaskCategory fromString(String value) {
        return TaskCategory.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
