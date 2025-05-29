package com.coinquyteam.shift.Data;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("SwapRequest")
public class SwapRequest
{
    @Id @Field("_idSwapRequest") private String idSwapRequest;
    @Field("houseId") private String houseId;
    @Field("assigmentA") private CleaningAssignment assignmentA;
    @Field("assigmentB")private CleaningAssignment assignmentB;
    @Field("accept") private StatusSwap acceptedByB;
    @Field("requestTime") private LocalDateTime requestTime;

    public SwapRequest() { }

    public SwapRequest(String houseId, CleaningAssignment assignmentA, CleaningAssignment assignmentB)
    {
        this.houseId = houseId;
        this.assignmentA = assignmentA;
        this.assignmentB = assignmentB;
        this.acceptedByB = StatusSwap.PENDING;
        this.requestTime = LocalDateTime.now();
    }

    public String getHouseId() { return this.houseId; }
    public void setHouseId(String houseId) { this.houseId = houseId; }

    public String getIdSwapRequest() { return this.idSwapRequest; }
    public void setIdSwapRequest(String idSwapRequest) { this.idSwapRequest = idSwapRequest; }

    public CleaningAssignment getAssignmentA() { return this.assignmentA; }
    public void setAssignmentA(CleaningAssignment assignmentA) { this.assignmentA = assignmentA; }

    public CleaningAssignment getAssignmentB() { return this.assignmentB; }
    public void setAssignmentB(CleaningAssignment assignmentB) { this.assignmentB = assignmentB; }

    public StatusSwap isAcceptedByB() { return this.acceptedByB; }
    public void setAcceptedByB(StatusSwap acceptedByB) { this.acceptedByB = acceptedByB; }

    public LocalDateTime getRequestTime() { return this.requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime;}
}
