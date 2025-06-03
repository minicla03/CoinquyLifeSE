package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class CleaningAssignment
{
    @PlanningId
    private Integer id;
    private HouseTask task;
    @PlanningVariable(valueRangeProviderRefs = {"roommateRange"})
    private Roommate assignedRoommate;
    private boolean isDone;

    public CleaningAssignment() {}

    public CleaningAssignment(Integer id, HouseTask task)
    {
        this.id = id;
        this.task = task;
        this.isDone = false;
    }

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public HouseTask getTask() { return task; }
    public void setTask(HouseTask task) { this.task = task; }

    public Roommate getAssignedRoommate() { return assignedRoommate; }
    public void setAssignedRoommate(Roommate assignedRoommate) { this.assignedRoommate = assignedRoommate;}

    public boolean isDone() { return isDone; }
    public void setDone(boolean done) {isDone = done; }
}