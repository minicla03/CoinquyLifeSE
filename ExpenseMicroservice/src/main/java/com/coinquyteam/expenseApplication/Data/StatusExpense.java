package com.coinquyteam.expenseApplication.Data;

public enum StatusExpense
{
    PENDING,
    SETTLED,
    CANCELLED;

    public static StatusExpense fromString(String status) {
        for (StatusExpense s : StatusExpense.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}