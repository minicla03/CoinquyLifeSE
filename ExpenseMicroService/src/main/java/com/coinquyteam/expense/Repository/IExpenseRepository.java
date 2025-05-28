package com.coinquyteam.expense.Repository;

import com.coinquyteam.expense.Data.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;


public interface IExpenseRepository extends MongoRepository<Expense, String> {
    
    @Query(value = "{ 'house_id' : ?0 }")
    List<Expense> findByHouseId(String houseId);
    
    @Query(value = "{ 'created_by' : ?0 }")
    List<Expense> findByCreatedBy(String userId);
    
    @Query(value = "{ 'participants' : ?0 }")
    List<Expense> findByParticipant(String userId);
    
    @Query(value = "{ 'house_id' : ?0, 'status' : ?1 }")
    List<Expense> findByHouseIdAndStatus(String houseId, String status);
    
    @Query(value = "{ 'participants' : ?0, 'status' : ?1 }")
    List<Expense> findByParticipantAndStatus(String userId, String status);
    
    @Query(value = "{ 'house_id' : ?0, 'created_date' : { $gte: ?1, $lte: ?2 } }")
    List<Expense> findByHouseIdAndDateRange(String houseId, Date startDate, Date endDate);
    
    @Query(value = "{ 'house_id' : ?0, 'category' : ?1 }")
    List<Expense> findByHouseIdAndCategory(String houseId, String category);
}
