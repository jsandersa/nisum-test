package com.jsanders.nisum.test.controller;

import com.jsanders.nisum.test.model.User;
import com.jsanders.nisum.test.repository.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/api/users", produces = "application/json")
@ResponseBody
public class UserController {
  @Autowired
  private UserService userService;

  @Value("${app.regexp}")
  private final String regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  Pattern pattern = Pattern.compile(regexp);

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  @PostMapping
  public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
    user.setToken(UUID.randomUUID());
    Map<String, Object> response = new HashMap<>();

    User userEmail = userService.existsEmail(user.getEmail());
    if (userEmail != null) {
      response.put("data", null);
      response.put("mensaje", String.format("Error: e-mail %s ya existe! - Status: %s", user.getEmail(), HttpStatus.CONFLICT));
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    Matcher matcher = pattern.matcher(user.getEmail());
    if (!matcher.matches()) {
      response.put("data", null);
      response.put("mensaje", String.format("Error: e-mail: %s no es válido! - Status: %s", user.getEmail(), HttpStatus.BAD_REQUEST));
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    response.put("mensaje", String.format("Usuario creado con éxito! - Status: %s", HttpStatus.CREATED));
    response.put("data", userService.create(user));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllUsers() {
    Map<String, Object> response = new HashMap<>();
    List<User> users = userService.getAll();
    response.put("data", users);
    response.put("mensaje", String.format("Recuperados %s registro(s) con éxito! - Status: %s", users.size(), HttpStatus.OK));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("id") UUID id) {
    Map<String, Object> response = new HashMap<>();
    try {
      response.put("data", userService.getById(id));
      response.put("mensaje", String.format("ID '%s' encontrado! - Staus: %s", id, HttpStatus.FOUND));
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      response.put("data", null);
      response.put("mensaje", String.format("Error: ID '%s' no encontrado! - Staus: %s", id, HttpStatus.NOT_FOUND));
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("id") UUID id, @RequestBody User user) {
    Map<String, Object> response = new HashMap<>();
    Optional<User> userOpt = Optional.ofNullable(userService.getById(id));
    if (userOpt != null) {
      User userEmail = userService.existsEmail(user.getEmail());
      if (userEmail != null) {
        response.put("data", null);
        response.put("mensaje", String.format("Error: e-mail %s ya existe! - Status: %s", user.getEmail(), HttpStatus.CONFLICT));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
      }

      Matcher matcher = pattern.matcher(user.getEmail());
      if (!matcher.matches()) {
        response.put("data", null);
        response.put("mensaje", String.format("Error: e-mail: %s no es válido! - Status: %s", user.getEmail(), HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      response.put("mensaje", String.format("User success actualized! - Status: %s", HttpStatus.CREATED));
      response.put("data", userService.update(user));
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    response.put("mensaje", String.format("Usuario creado con éxito! - Status: %s", HttpStatus.CREATED));
    response.put("data", userService.create(user));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable("id") UUID id) {
    Map<String, Object> response = new HashMap<>();

    User user = userService.getById(id);
    if (user == null) {
      response.put("data", null);
      response.put("mensaje", String.format("Error: ID '%s' no encontrado! - Staus: %s", id, HttpStatus.NOT_FOUND));
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    try {
      userService.delete(id);
      response.put("data", HttpStatus.NO_CONTENT);
      response.put("mensaje", String.format("ID '%s' eliminado! - Staus: %s", id, HttpStatus.OK));
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      response.put("data", null);
      response.put("mensaje", String.format("Error: ID '%s' no encontrado! - Staus: %s", id, HttpStatus.INTERNAL_SERVER_ERROR));
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
