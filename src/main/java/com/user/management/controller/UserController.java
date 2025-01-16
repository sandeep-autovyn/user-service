package com.user.management.controller;


import com.user.management.entity.RandomUser;
import com.user.management.entity.User;
import com.user.management.service.UserService;
import com.user.management.util.Filter;
import com.user.management.util.Pagination;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(@RequestBody Filter filter) {
        log.info("getting all users");
        Page<User> employees = userService.getAllUsers(Pagination.getPageable(filter));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<User> getUser(@PathVariable String userName) {
        log.info("getting user by user name :{}", userName);
        User user = userService.getUserByUserName(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        log.info("creating new user");
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/user/{userName}")
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable String userName) {
        log.info("updating user for user name :{}", userName);
        return new ResponseEntity<>(userService.updateUser(user, userName), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<String> deleteUser(@PathVariable String userName) {
        log.info("deleting user for user name :{}", userName);
        userService.deleteUser(userName);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/randomuser/{count}")
    public List<RandomUser> getRandomUsers(@PathVariable int count) {
        log.info("getting random users");
        return userService.getRandomUsers(count);
    }

    @GetMapping("/randomuser/tree/{count}")
    public Map<String, Map<String, Map<String, List<RandomUser>>>> getRandomUsersTree(@PathVariable int count) {
        log.info("getting random users");
        return userService.getRandomUsersTree(count);
    }
}
