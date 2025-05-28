package com.coinquyteam.shift.OptaPlanner;

import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;

/*
There will be exactly one instance of this class per planning solution.
The planning solution and the constraint configuration have a one-to-one relationship,
but they serve a different purpose, so they aren’t merged into a single class.
 */
public class ScheduleConstraintProvider implements ConstraintProvider
{
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory)
    {
        return new Constraint[]{
                roommateUnavailable(factory),
                unassignedTask(factory),
                unevenDistribution(factory),
                noConsecutiveDays(factory)
        };
    }

    private Constraint roommateUnavailable(ConstraintFactory factory)
    {
        return factory.forEach(CleaningAssignment.class)
                .filter(a -> a.getAssignedRoommate() != null &&
                        a.getAssignedRoommate().getUnavailableTimeSlots().stream()
                                .anyMatch(u -> u.overlaps(a.getTask().getTimeSlot())))
                .penalizeConfigurable("Roommate unavailable");
    }

    private Constraint unassignedTask(ConstraintFactory factory)
    {
        return factory.forEach(CleaningAssignment.class)
                .filter(a -> a.getAssignedRoommate() == null)
                .penalize("Unassigned task");
    }

    private Constraint unevenDistribution(ConstraintFactory factory)
    {
        return factory.forEach(CleaningAssignment.class)
                .filter(a -> a.getAssignedRoommate() != null)
                .groupBy(CleaningAssignment::getAssignedRoommate, count())
                .penalizeConfigurable("Uneven workload",
                        (roommate, count) -> count * count);
    }

    private Constraint noConsecutiveDays(ConstraintFactory factory)
    {
        return factory.forEachUniquePair(CleaningAssignment.class, equal(CleaningAssignment::getAssignedRoommate))
                .filter((a1, a2) -> {
                    if (a1.getAssignedRoommate() == null || a2.getAssignedRoommate() == null) return false;
                    int day1 = a1.getTask().getTimeSlot().getStart().getDayOfYear();
                    int day2 = a2.getTask().getTimeSlot().getStart().getDayOfYear();
                    return Math.abs(day1 - day2) == 1;
                })
                .penalizeConfigurable("Consecutive cleaning days");
    }
}