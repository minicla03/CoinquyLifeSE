package com.coinquyteam.shift.Data;

import java.time.LocalDateTime;

public class TimeSlot
{
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeSlot() {}

    public TimeSlot(LocalDateTime start, LocalDateTime end)
    {
        this.start = start;
        this.end = end;
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
