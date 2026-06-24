package com.finance.repo;
import com.finance.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
  
}
