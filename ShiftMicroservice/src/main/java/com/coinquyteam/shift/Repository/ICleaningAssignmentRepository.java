package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ICleaningAssignmentRepository extends MongoRepository<CleaningAssignment, Integer>
{

}