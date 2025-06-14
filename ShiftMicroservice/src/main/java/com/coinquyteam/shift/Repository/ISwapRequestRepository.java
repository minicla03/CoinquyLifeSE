package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.Data.SwapRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ISwapRequestRepository extends MongoRepository<SwapRequest, String>
{
    //Null Object
    @Query("{ 'houseId' : ?0 }")
    List<SwapRequest> findAllByHouseId(String houseId);
}
