package com.finance.repo;
import com.finance.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SavingsGoalRepo extends JpaRepository<SavingsGoal, Long> {
  
}
