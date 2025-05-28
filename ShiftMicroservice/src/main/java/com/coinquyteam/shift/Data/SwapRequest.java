package com.coinquyteam.shift.Data;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;

import java.time.LocalDateTime;

public class SwapRequest
{
    private CleaningAssignment assignmentA;  // turno di A
    private CleaningAssignment assignmentB;  // turno di B
    private boolean acceptedByB;              // B accetta lo scambio
    private LocalDateTime requestTime;        // quando è stata fatta la richiesta

    public SwapRequest(CleaningAssignment assignmentA, CleaningAssignment assignmentB, boolean b)
    {
        this.assignmentA = assignmentA;
        this.assignmentB = assignmentB;
        this.acceptedByB = false;
        this.requestTime = LocalDateTime.now();
    }

    public CleaningAssignment getAssignmentA() { return this.assignmentA; }
    public void setAssignmentA(CleaningAssignment assignmentA) { this.assignmentA = assignmentA; }

    public CleaningAssignment getAssignmentB() { return this.assignmentB; }
    public void setAssignmentB(CleaningAssignment assignmentB) { this.assignmentB = assignmentB; }

    public boolean isAcceptedByB() { return this.acceptedByB; }
    public void setAcceptedByB(boolean acceptedByB) { this.acceptedByB = acceptedByB; }

    public LocalDateTime getRequestTime() { return this.requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime;}
}
