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
    @Field("assigmentA") private CleaningAssignment assignmentA;
    @Field("assigmentb")private CleaningAssignment assignmentB;
    @Field("accept") private boolean acceptedByB;
    @Field("requestTime") private LocalDateTime requestTime;

    public SwapRequest() { }

    public SwapRequest(CleaningAssignment assignmentA, CleaningAssignment assignmentB, boolean b)
    {
        this.assignmentA = assignmentA;
        this.assignmentB = assignmentB;
        this.acceptedByB = false;
        this.requestTime = LocalDateTime.now();
    }

    public String getIdSwapRequest() { return this.idSwapRequest; }
    public void setIdSwapRequest(String idSwapRequest) { this.idSwapRequest = idSwapRequest; }

    public CleaningAssignment getAssignmentA() { return this.assignmentA; }
    public void setAssignmentA(CleaningAssignment assignmentA) { this.assignmentA = assignmentA; }

    public CleaningAssignment getAssignmentB() { return this.assignmentB; }
    public void setAssignmentB(CleaningAssignment assignmentB) { this.assignmentB = assignmentB; }

    public boolean isAcceptedByB() { return this.acceptedByB; }
    public void setAcceptedByB(boolean acceptedByB) { this.acceptedByB = acceptedByB; }

    public LocalDateTime getRequestTime() { return this.requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime;}
}
