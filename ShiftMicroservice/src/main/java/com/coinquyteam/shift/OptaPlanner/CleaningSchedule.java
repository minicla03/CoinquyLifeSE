package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.CleaningTask;
import com.coinquyteam.shift.Data.Roommate;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class CleaningSchedule {
    @ValueRangeProvider(id = "roommateRange")
    private List<Roommate> roommateList;

    private List<CleaningTask> taskList;

    @PlanningEntityCollectionProperty
    private List<CleaningAssignment> assignmentList;

    @PlanningScore
    private HardSoftScore score;

    public List<Roommate> getRoommateList() { return roommateList; }
    public void setRoommateList(List<Roommate> roommateList) { this.roommateList = roommateList; }

    public List<CleaningTask> getTaskList() { return taskList; }
    public void setTaskList(List<CleaningTask> taskList) { this.taskList = taskList; }

    public List<CleaningAssignment> getAssignmentList() { return assignmentList; }
    public void setAssignmentList(List<CleaningAssignment> assignmentList) { this.assignmentList = assignmentList; }

    public HardSoftScore getScore() { return score; }
    public void setScore(HardSoftScore score) { this.score = score; }
}