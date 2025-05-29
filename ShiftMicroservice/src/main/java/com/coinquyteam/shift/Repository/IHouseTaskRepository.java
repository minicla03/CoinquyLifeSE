package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.Data.HouseTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IHouseTaskRepository extends MongoRepository<HouseTask, String>
{
    @Query("{ 'houseId' : ?0 }")
    List<HouseTask> findAllByHouseId(String houseId);
}
