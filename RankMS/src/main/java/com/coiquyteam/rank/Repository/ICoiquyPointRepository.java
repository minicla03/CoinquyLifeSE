package com.coiquyteam.rank.Repository;

import com.coiquyteam.rank.Data.CoinquyPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ICoiquyPointRepository extends MongoRepository<CoinquyPoint, String>
{
    @Query("{ 'houseId': ?0 }")
    List<CoinquyPoint> findByHouseId(String houseId);
}
