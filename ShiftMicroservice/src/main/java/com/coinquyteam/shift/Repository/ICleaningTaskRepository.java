package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.Data.CleaningTask;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICleaningTaskRepository extends MongoRepository<CleaningTask, String>
{

}
