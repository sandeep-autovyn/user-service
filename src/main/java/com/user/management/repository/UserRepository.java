package com.user.management.repository;


import com.user.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, PagingAndSortingRepository<User,Integer> {
    Optional<User> findByUserName(String userName);
}
