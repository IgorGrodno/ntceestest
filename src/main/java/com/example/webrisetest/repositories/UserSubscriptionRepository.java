package com.example.webrisetest.repositories;

import com.example.webrisetest.models.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    void deleteBySubscriptionId(Long subscriptionId);
}
