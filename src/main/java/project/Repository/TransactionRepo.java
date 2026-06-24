package com.finance.repo;
import com.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate; import java.util.List;
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDateBetween(LocalDate s, LocalDate e);
    List<Transaction> findByCategory(String category);
}
