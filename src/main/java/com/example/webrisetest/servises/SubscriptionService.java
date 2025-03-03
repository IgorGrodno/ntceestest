package com.example.webrisetest.servises;

import com.example.webrisetest.models.Subscription;
import com.example.webrisetest.models.User;
import com.example.webrisetest.repositories.SubscriptionRepository;
import com.example.webrisetest.repositories.UserRepository;
import com.example.webrisetest.repositories.UserSubscriptionRepository; // Добавьте репозиторий для промежуточной таблицы
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubscriptionService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

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

    public Iterable<User> getSubscriptionUsers(Long subscriptionId) {
        Subscription subscription = getSubscriptionById(subscriptionId);
        if(subscription == null) return null;
        return subscription.getUsers();
    }

    public void deleteSubscription(Long subscriptionId) {
        userSubscriptionRepository.deleteBySubscriptionId(subscriptionId);
        subscriptionRepository.deleteById(subscriptionId);
    }

    public Iterable<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> getTopSubscriptions(int count) {
        return subscriptionRepository.findTopSubscriptions(PageRequest.of(0, count));
    }
}
