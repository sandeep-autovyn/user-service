package com.user.management.persistence;

import com.user.management.entity.User;
import com.user.management.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistenceProvider implements UserPersistenceService {

    private final UserRepository userRepository;

    public UserPersistenceProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);

    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
