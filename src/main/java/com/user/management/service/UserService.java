package com.user.management.service;

import com.user.management.entity.RandomUser;
import com.user.management.entity.User;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface UserService {

    Page<User> getAllUsers(Pageable pageable);

    User getUserByUserName(String userName);

    User createUser(User user);

    User updateUser(User user, String userName);

    void deleteUser(String userName);

    List<RandomUser> getRandomUsers(int size);


    Map<String, Map<String, Map<String, List<RandomUser>>>> getRandomUsersTree(int count);
}
