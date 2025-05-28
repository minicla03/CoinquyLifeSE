package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.CleaningTask;
import com.coinquyteam.shift.Data.Roommate;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class CleaningAssignment
{
    private CleaningTask task;

    @PlanningVariable(valueRangeProviderRefs = {"roommateRange"})
    private Roommate assignedRoommate;

    public CleaningAssignment() {}

    public CleaningAssignment(CleaningTask task) {
        this.task = task;
    }

    public CleaningTask getTask() { return task; }
    public void setTask(CleaningTask task) { this.task = task; }

    public Roommate getAssignedRoommate() { return assignedRoommate; }
    public void setAssignedRoommate(Roommate assignedRoommate) { this.assignedRoommate = assignedRoommate;}
}