package com.example.webrisetest.repositories;

import com.example.webrisetest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u")
    Page<User> getAllUsersPageable(Pageable pageable);
}


