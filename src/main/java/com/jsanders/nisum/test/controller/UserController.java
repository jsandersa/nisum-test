package com.jsanders.nisum.test.controller;

import com.jsanders.nisum.test.model.User;
import com.jsanders.nisum.test.repository.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

import static com.jsanders.nisum.test.error_handling.Validator.check;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json", consumes = "application/json")
@ResponseBody
public class UserController {

  Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private final UserService userService;

  Pattern emailPattern;
  Pattern passwordPattern;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setEmailPattern(@Value("${user.email.regexp}") String userEmailRegexp) {
    logger.info("userEmailRegexp: {}", userEmailRegexp);
    emailPattern = Pattern.compile(userEmailRegexp);
  }

  @Autowired
  public void setPasswordPattern(@Value("${user.password.regexp}") String userPasswordRegexp) {
    logger.info("userPasswordRegexp: {}", userPasswordRegexp);
    passwordPattern = Pattern.compile(userPasswordRegexp);
  }

  boolean validPattern(Pattern pattern, String text) {
    return pattern.matcher(text).matches();
  }

  void checkEmailUpdate(User user) {
    User emailUser = userService.getById(user.getId());
    if (emailUser != null && !emailUser.getEmail().equals(user.getEmail())) {
      check(!userService.existsEmail(user.getEmail()), "e-mail address already registered");
    }
  }

  void validateUser(User user, boolean update) {
    check(user != null, "user object is null");
    if (update) {
      checkEmailUpdate(user);
    } else {
      check(validPattern(emailPattern, user.getEmail()), "invalid email '%s'", user.getEmail());
    }
    check(validPattern(passwordPattern, user.getPassword()), "invalid password");
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    user.setToken(UUID.randomUUID());
    validateUser(user, false);
    return ResponseEntity.ok(userService.create(user));
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") UUID id, @RequestBody User user) {
    check(user.getId().equals(id), "object ID mismatch");
    validateUser(user, true);
    return ResponseEntity.ok(userService.update(user));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<Object> deleteUserById(@PathVariable("id") UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
