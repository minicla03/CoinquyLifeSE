package com.coinquyteam.shift.Data;

import com.coinquyteam.shift.Data.TimeSlot;

import java.util.List;

public class Roommate
{
    private String name;
    private List<TimeSlot> unavailableTimeSlots;

    public Roommate() {}

    public Roommate(String name, List<TimeSlot> unavailableTimeSlots) {
        this.name = name;
        this.unavailableTimeSlots = unavailableTimeSlots;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public List<TimeSlot> getUnavailableTimeSlots() { return unavailableTimeSlots; }
    public void setUnavailableTimeSlots(List<TimeSlot> unavailableTimeSlots) { this.unavailableTimeSlots = unavailableTimeSlots; }
}
