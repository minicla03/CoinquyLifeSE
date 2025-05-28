package com.coinquyteam.shift.OptaPlanner;

import com.coinquyteam.shift.Data.SwapRequest;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.Joiners.equal;

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
                noConsecutiveDays(factory),
                swapRequestBeforeDeadline(factory),
                swapRequestSatisfied(factory)
        };
    }

    //Hard constraints
    private Constraint roommateUnavailable(ConstraintFactory factory)
    {
        return factory.forEach(CleaningAssignment.class)
                .filter(a -> a.getAssignedRoommate() != null &&
                        a.getAssignedRoommate().getUnavailableTimeSlots().stream()
                                .anyMatch(u -> u.overlaps(a.getTask().getTimeSlot())))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Roommate unavailable");
    }

    //Soft constraints
    private Constraint unassignedTask(ConstraintFactory factory)
    {
        return factory.forEach(CleaningAssignment.class)
                .filter(a -> a.getAssignedRoommate() == null)
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Unassigned task");
    }

    //Soft constraint
    private Constraint unevenDistribution(ConstraintFactory factory)
    {
        return factory.forEach(CleaningAssignment.class)
                .filter(a -> a.getAssignedRoommate() != null)
                .groupBy(CleaningAssignment::getAssignedRoommate, count())
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Uneven distribution of tasks");
    }

    //Hard constraint
    private Constraint noConsecutiveDays(ConstraintFactory factory) {
        return factory.forEachUniquePair(CleaningAssignment.class, equal(CleaningAssignment::getAssignedRoommate))
                .filter((a1, a2) -> {
                    if (a1.getAssignedRoommate() == null || a2.getAssignedRoommate() == null) return false;
                    long daysBetween = ChronoUnit.DAYS.between(
                            a1.getTask().getTimeSlot().getStart().toLocalDate(),
                            a2.getTask().getTimeSlot().getStart().toLocalDate());
                    return Math.abs(daysBetween) == 1;
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Consecutive cleaning days");
    }

    //Hard constraint
    private Constraint swapRequestBeforeDeadline(ConstraintFactory factory)
    {
        return factory.forEach(SwapRequest.class)
                .filter(SwapRequest::isAcceptedByB)
                .filter(req -> {
                    // Controlla che la richiesta sia fatta almeno un giorno prima del turno
                    LocalDateTime requestTime = req.getRequestTime();
                    LocalDateTime shiftStart = req.getAssignmentA().getTask().getTimeSlot().getStart();
                    return requestTime.isAfter(shiftStart.minusDays(1));
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Swap request before deadline");
    }

    // Hard constraint
    private Constraint swapRequestSatisfied(ConstraintFactory factory)
    {
        return factory.forEach(SwapRequest.class)
                .filter(SwapRequest::isAcceptedByB)
                .filter(req -> {
                    CleaningAssignment a1 = req.getAssignmentA();
                    CleaningAssignment a2 = req.getAssignmentB();
                    // Verifica se i coinquilini si sono scambiati realmente
                    return !(a1.getAssignedRoommate().equals(req.getAssignmentB().getAssignedRoommate()) &&
                            a2.getAssignedRoommate().equals(req.getAssignmentA().getAssignedRoommate()));
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Swap request not satisfied");
    }
}