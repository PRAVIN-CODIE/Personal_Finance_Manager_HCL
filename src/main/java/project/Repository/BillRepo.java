package com.finance.repo;
import com.finance.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BillRepo extends JpaRepository<Bill, Long> {
  
}
