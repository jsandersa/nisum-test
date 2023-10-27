package com.jsanders.nisum.test.controller;

import com.jsanders.nisum.test.model.User;
import com.jsanders.nisum.test.repository.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

import static com.jsanders.nisum.test.error_handling.Validator.check;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
@ResponseBody
public class UserController {

  Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

//  private String regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  Pattern emailPattern;
  Pattern passwordPattern;

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

  void validateUser(User user) {
    check(user != null, "user object is null");
    check(!userService.existsEmail(user.getEmail()), "e-mail address already registered");
    check(validPattern(emailPattern, user.getEmail()), "invalid email '%s'", user.getEmail());
    check(validPattern(passwordPattern, user.getPassword()), "invalid password");
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    user.setToken(UUID.randomUUID());
    validateUser(user);
    return new ResponseEntity<>(userService.create(user), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") UUID id) {
      return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") UUID id, @RequestBody User user) {
    check(user.getId().equals(id), "object ID mismatch");
    validateUser(user);
    return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<Object> deleteUserById(@PathVariable("id") UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
