package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.Data.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITimeSlotRepository extends MongoRepository<TimeSlot, Integer>
{

}

