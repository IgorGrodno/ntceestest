package com.example.webrisetest.servises;

import com.example.webrisetest.DTOs.UserDTO;
import com.example.webrisetest.models.Subscription;
import com.example.webrisetest.models.User;
import com.example.webrisetest.repositories.SubscriptionRepository;
import com.example.webrisetest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public Iterable<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user ->
                new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail())).toList();
    }

    public Page<UserDTO> getUsersPageable(int pageNumber, int pageSize) {
        return userRepository.getAllUsersPageable(PageRequest.of(pageNumber, pageSize)).map(user ->
                new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public User addSubscriptionToUser(Long userId, Long subscriptionId) {
        User user = getUserById(userId);
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);

        if (user != null && subscription != null) {
            user.getSubscriptions().add(subscription);
            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public User removeSubscriptionFromUser(Long userId, Long subscriptionId) {
        User user = getUserById(userId);
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);

        if (user != null && subscription != null) {
            user.getSubscriptions().remove(subscription);
            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public Set<Subscription> getUserSubscriptions(Long userId) {
        User user = getUserById(userId);
        if (user == null) return null;
        Hibernate.initialize(user.getSubscriptions());
        return user.getSubscriptions();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, User newUserData) {
        User user = getUserById(userId);
        if (user == null) return null;
        user.setFirstName(newUserData.getFirstName());
        user.setLastName(newUserData.getLastName());
        if (newUserData.getEmail() != null) {
            user.setEmail(newUserData.getEmail());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getSubscriptions().forEach(subscription -> subscription.getUsers().remove(user));
            user.getSubscriptions().clear();
            userRepository.delete(user);
        }
    }
}
