package com.coinquyteam.shift.OptaPlanner;

@PlanningSolution
public class CleaningSchedule {

    @ValueRangeProvider(id = "roommateRange")
    private List<Roommate> roommateList;

    private List<CleaningTask> taskList;

    @PlanningEntityCollectionProperty
    private List<CleaningAssignment> assignmentList;

    private HardSoftScore score;
}
