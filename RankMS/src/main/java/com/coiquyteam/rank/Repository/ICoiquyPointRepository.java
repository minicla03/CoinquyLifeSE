package com.coiquyteam.rank.Repository;

import com.coiquyteam.rank.Data.CoinquyPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ICoiquyPointRepository extends MongoRepository<CoinquyPoint, String>
{

}
