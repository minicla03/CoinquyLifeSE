package com.coinquyteam.shift.OptaPlanner;

@PlanningEntity
public class CleaningAssignment {
    private CleaningTask task;

    @PlanningVariable(valueRangeProviderRefs = {"roommateRange"})
    private Roommate assignedRoommate;
}
