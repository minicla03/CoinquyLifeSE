package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.HouseTask;
import com.coinquyteam.shift.Data.Roommate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@Entity(name = "CleaningAssignment")
public class CleaningAssignment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idJPA;

    @PlanningId
    private Integer id;
    //TODO: Change to HouseTask
    private HouseTask task;
    @PlanningVariable(valueRangeProviderRefs = {"roommateRange"})
    private Roommate assignedRoommate;

    public CleaningAssignment() {}

    public CleaningAssignment(Integer id, HouseTask task)
    {
        this.id = id;
        this.task = task;
    }

    public Integer getIdJPA() { return idJPA; }

    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

    public HouseTask getTask() { return task; }
    public void setTask(HouseTask task) { this.task = task; }

    public Roommate getAssignedRoommate() { return assignedRoommate; }
    public void setAssignedRoommate(Roommate assignedRoommate) { this.assignedRoommate = assignedRoommate;}
}