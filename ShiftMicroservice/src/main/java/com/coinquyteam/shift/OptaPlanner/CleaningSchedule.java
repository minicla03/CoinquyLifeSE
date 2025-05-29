package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import com.coinquyteam.shift.Data.SwapRequest;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
@Component
public class CleaningSchedule
{
    @ValueRangeProvider(id = "roommateRange")
    private List<Roommate> roommateList;

    private List<HouseTask> taskList;

    @PlanningEntityCollectionProperty
    private List<CleaningAssignment> assignmentList;

    @PlanningScore
    private HardSoftScore score;

    @ProblemFactCollectionProperty
    private List<SwapRequest> swapRequestList=new ArrayList<>();


    public List<Roommate> getRoommateList() { return roommateList; }
    public void setRoommateList(List<Roommate> roommateList) { this.roommateList = roommateList; }

    public List<HouseTask> getTaskList() { return taskList; }
    public void setTaskList(List<HouseTask> taskList) { this.taskList = taskList; }

    public List<CleaningAssignment> getAssignmentList() { return assignmentList; }
    public void setAssignmentList(List<CleaningAssignment> assignmentList) { this.assignmentList = assignmentList; }

    public HardSoftScore getScore() { return score; }
    public void setScore(HardSoftScore score) { this.score = score; }

    public List<SwapRequest> getSwapRequestList() {
        return this.swapRequestList;
    }

    public void setSwapRequestList(List<SwapRequest> swapRequestList) {
        this.swapRequestList = swapRequestList;
    }
}