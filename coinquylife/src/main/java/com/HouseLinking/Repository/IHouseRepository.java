package com.HouseLinking.Repository;

import com.HouseLinking.Data.House;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IHouseRepository extends MongoRepository<House, String> {

    @Query(value = "{ 'houseId' : ?0 }")
    House findByHouseId(String houseId);

    @Query(value = "{ 'houseName' : ?0 }")
    House findByHouseName(String houseName);

    @Query(value = "{ 'houseAddress' : ?0 }")
    House findByHouseAddress(String houseAddress);

    @Query(value = "{ 'houseCode' : ?0 }")
    House findByHouseCode(String houseCode);

}
