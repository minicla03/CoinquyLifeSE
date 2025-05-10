package com.coinquylifeteam.auth.Repository;

import com.coinquylifeteam.auth.Data.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

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

}