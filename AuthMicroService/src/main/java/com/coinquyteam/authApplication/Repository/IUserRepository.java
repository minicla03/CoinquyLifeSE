package com.coinquyteam.authApplication.Repository;

import com.coinquyteam.authApplication.Data.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface IUserRepository extends MongoRepository<User, String>
{

    @Query(value = "{ 'email' : ?0 }")
    User findByEmail(String email);

    @Query(value = "{ 'username' : ?0 }")
    User findByUsername(String username);

    @Query(value = "{ 'id_user' : ?0 }")
    User findByIdUser(String id_user);

    @Query(value = "{ 'house_user' : ?0 }")
    User findByHouseUser(String house_user);

    @Update("{ '$set' : { 'house_user' : ?1 } }")
    @Query("{ 'username' : ?0 }")
    void setHouseUser(String username, String houseCode);

}