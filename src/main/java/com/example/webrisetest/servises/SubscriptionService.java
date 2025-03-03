package com.example.webrisetest.servises;

import com.example.webrisetest.models.Subscription;
import com.example.webrisetest.repositories.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Subscription getSubscriptionById(Long subscriptionId) {
        if (subscriptionRepository.findById(subscriptionId).isEmpty()) {
            return null;
        } else {
            return subscriptionRepository.findById(subscriptionId).get();
        }
    }

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Iterable<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> getTopSubscriptions(int count) {
        return subscriptionRepository.findTopSubscriptions(PageRequest.of(0, count));
    }
}
