package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.Data.CleaningTask;
import com.coinquyteam.shift.Data.Roommate;
import com.coinquyteam.shift.Data.Tasks;
import com.coinquyteam.shift.Data.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IShiftRepository extends MongoRepository<CleaningTask, String>
{
    @Query("{ 'assignedRoommate' : ?0 }")
    List<CleaningTask> findByAssignedRoommate(Roommate roommate);

    @Query("{ 'timeSlot' : ?0 }")
    List<CleaningTask> findByTimeSlot(TimeSlot timeSlot);

    @Query("{ 'assignedRoommate' : ?0, 'timeSlot' : ?1 }")
    List<CleaningTask> findByAssignedRoommateAndTimeSlot(Roommate roommate, TimeSlot timeSlot);

    @Query("{ 'task' : ?0 }")
    List<CleaningTask> findByTask(Tasks task);

    @Query("{ 'task' : ?0, 'assignedRoommate' : ?1 }")
    List<CleaningTask> findByTaskAndAssignedRoommate(Tasks task, Roommate roommate);
}
