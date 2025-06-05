package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@PlanningEntity
@Document("CleaningAssignment")
public class CleaningAssignment
{
    @PlanningId
    private String id;

    @Field("problemId")
    private UUID problemId;

    @Field("task")
    private HouseTask task;

    @Field("assignedRoommate")
    @PlanningVariable(valueRangeProviderRefs = {"roommateRange"})
    private Roommate assignedRoommate;

    public CleaningAssignment() {}

    public CleaningAssignment(UUID problemId,String id, HouseTask task)
    {
        this.problemId = problemId;
        this.id = id;
        this.task = task;
    }

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    public HouseTask getTask() { return task; }
    public void setTask(HouseTask task) { this.task = task; }

    public UUID getProblemId() { return problemId; }
    public void setProblemId(UUID problemId) { this.problemId = problemId; }

    public Roommate getAssignedRoommate() { return assignedRoommate; }
    public void setAssignedRoommate(Roommate assignedRoommate) { this.assignedRoommate = assignedRoommate;}
}