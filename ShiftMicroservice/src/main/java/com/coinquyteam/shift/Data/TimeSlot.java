package com.coinquyteam.shift.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("TimeSlot")
public class TimeSlot
{
    @Id
    @Field("_idTimeSlot") private String idTimeSlot;
    @Field("start") private LocalDateTime start;
    @Field("end") private LocalDateTime end;

    public TimeSlot() {}

    public TimeSlot(LocalDateTime start, LocalDateTime end)
    {
        this.start = start;
        this.end = end;
    }

    public String getIdTimeSlot()
    {
        return this.idTimeSlot;
    }

    public void setIdTimeSlot(String idTimeSlot)
    {
        this.idTimeSlot = idTimeSlot;
    }

    public LocalDateTime getStart()
    {
        return start;
    }
    public void setStart(LocalDateTime start)
    {
        this.start = start;
    }

    public LocalDateTime getEnd()
    {
        return end;

    }
    public void setEnd(LocalDateTime end)
    {
        this.end = end;
    }

    public boolean overlaps(TimeSlot other)
    {
        return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
    }
}
