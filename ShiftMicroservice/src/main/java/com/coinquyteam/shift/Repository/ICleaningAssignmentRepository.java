package com.coinquyteam.shift.Repository;

import com.coinquyteam.shift.OptaPlanner.CleaningAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICleaningAssignmentRepository extends JpaRepository<CleaningAssignment, Integer>
{

}