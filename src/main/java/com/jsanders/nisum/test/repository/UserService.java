package com.jsanders.nisum.test.repository;

import com.jsanders.nisum.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User create(User user) {
    return userRepository.save(user);
  }

  public User getById(UUID id) {
    Optional<User> optUser = userRepository.findById(id);
    return optUser.get();
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public void delete(UUID id) {
    userRepository.deleteById(id);
  }

  public User existsEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User update(User user) { return userRepository.save(user); }
}
