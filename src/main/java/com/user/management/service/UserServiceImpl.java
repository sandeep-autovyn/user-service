package com.user.management.service;


import com.user.management.entity.RandomUser;
import com.user.management.entity.User;
import com.user.management.exception.UserNameAlreadyExists;
import com.user.management.exception.UserNotFoundException;
import com.user.management.persistence.UserPersistenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserPersistenceService userPersistenceService;
    private final RandomUserGenerationService randomUserGenerationService;

    public UserServiceImpl(UserPersistenceService userPersistenceService, RandomUserGenerationService randomUserGenerationService) {
        this.userPersistenceService = userPersistenceService;
        this.randomUserGenerationService = randomUserGenerationService;
    }


    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userPersistenceService.getAllUsers(pageable);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userPersistenceService.getUserByUserName(userName).orElseThrow(() -> new UserNotFoundException("User not found for user name : " + userName)
        );
    }

    @Override
    public User createUser(User user) {
        validateUserName(user.getUserName());
        return saveUser(user);
    }


    @Override
    public User updateUser(User user, String userName) {
        User existingUser = getUserByUserName(userName);
        validateUserName(user.getUserName());
        return saveUser(user.toBuilder().id(existingUser.getId()).build());
    }

    @Override
    public void deleteUser(String userName) {
        userPersistenceService.deleteUser(getUserByUserName(userName));
    }

    @Override
    public List<RandomUser> getRandomUsers(int size) {
        return randomUserGenerationService.generateRandomUsers(size).getResults();
    }

    @Override
    public Map<String, Map<String, Map<String, List<RandomUser>>>> getRandomUsersTree(int count) {
        return getRandomUsers(count).stream()
                .collect(Collectors.groupingBy(
                        user -> user.getLocation().getCountry(),
                        Collectors.groupingBy(
                                user -> user.getLocation().getState(),
                                Collectors.groupingBy(
                                        user -> user.getLocation().getCity()
                                )
                        )
                ));

    }

    private User saveUser(User user) {
        return userPersistenceService.createUser(user);
    }

    private void validateUserName(String userName) {
        userPersistenceService.getUserByUserName(userName).ifPresent(e -> {
            throw new UserNameAlreadyExists("User already exists for user name : " + userName);
        });
    }

}
