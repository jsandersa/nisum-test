package com.jsanders.nisum.test.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  @Column(name = "email")
  private String email;
  private String password;
  private UUID token;
  @CreationTimestamp
  private LocalDateTime created;
  @UpdateTimestamp
  private LocalDateTime modified;
  @CreationTimestamp
  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
          cascade = {
                  CascadeType.MERGE,
                  CascadeType.PERSIST,
                  CascadeType.REMOVE
          })
  private List<Phone> phones;

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setToken(UUID token) {
    this.token = token;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public LocalDateTime getModified() {
    return modified;
  }

  public void setModified(LocalDateTime modified) {
    this.modified = modified;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }
}
