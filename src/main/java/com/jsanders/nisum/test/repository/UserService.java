package com.jsanders.nisum.test.repository;

import com.jsanders.nisum.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

  public boolean existsEmail(String email) {
    return userRepository.findByEmail(email) != null;
  }

  public User update(User userUpdate) {
    User user = userRepository.findById(userUpdate.getId()).get();
    user.setName(userUpdate.getName());
    user.setPassword(userUpdate.getPassword());
    user.setEmail(userUpdate.getEmail());
    user.setModified(LocalDateTime.now());
    return userRepository.save(user);
  }
}
