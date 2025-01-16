package com.user.management.persistence;

import com.user.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface UserPersistenceService {

    public Page<User> getAllUsers(Pageable pageable);

    public Optional<User> getUserByUserName(String userName);

    public User createUser(User user);

    public void deleteUser(User userName);


}
