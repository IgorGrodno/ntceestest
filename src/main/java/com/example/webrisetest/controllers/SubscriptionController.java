package com.example.webrisetest.controllers;

import com.example.webrisetest.models.Subscription;
import com.example.webrisetest.servises.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")

public class SubscriptionController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        if (createdSubscription != null) {
            return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
        } else {
            logger.error("Subscription creation failed for subscription: {}", subscription);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        if (subscription != null) {
            return new ResponseEntity<>(subscription, HttpStatus.OK);
        } else {
            logger.warn("No subscription found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<Iterable<Subscription>> getSubscriptions() {
        Iterable<Subscription> subscriptions = subscriptionService.getSubscriptions();
        if (subscriptions != null) {
            return new ResponseEntity<>(subscriptions, HttpStatus.OK);
        } else {
            logger.warn("No subscriptions found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/top")
    public List<Subscription> getTopSubscriptions(@RequestParam(defaultValue = "3") int count) {
        return subscriptionService.getTopSubscriptions(count);
    }
}
