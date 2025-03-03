package com.example.webrisetest.repositories;

import com.example.webrisetest.models.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s ORDER BY SIZE(s.users) DESC")
    List<Subscription> findTopSubscriptions(Pageable pageable);
}
