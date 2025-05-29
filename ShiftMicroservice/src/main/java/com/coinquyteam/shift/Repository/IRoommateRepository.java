package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.Data.Roommate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IRoommateRepository extends MongoRepository<Roommate, String>
{
    @Query("{ 'houseId' : ?0 }")
    List<Roommate> findAllByHouseId(String houseId);
}
