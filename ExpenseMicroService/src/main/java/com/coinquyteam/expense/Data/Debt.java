package com.coinquyteam.expense.Data;

import java.util.Map;

public class Debt {
    private String createdBy;  // Chi ha fatto la spesa
    private Map<String, Double> participants; // Chi deve dei soldi e quanto

    public Debt( String createdBy, Map<String, Double> participants) {
        this.createdBy = createdBy;
        this.participants = participants;
    }

    public Debt() {

    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Double> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Double> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "createdBy='" + createdBy + '\'' +
                ", participants=" + participants +
                '}';
    }
}

