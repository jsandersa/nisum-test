package com.jsanders.nisum.test.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer number;
  private Integer citycode;
  private Integer countrycode;

  @JsonBackReference
  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "user_id")
  private User user;

  public Long getId() {
    return id;
  }

  public Integer getNumber() {
    return number;
  }

  public Integer getCitycode() {
    return citycode;
  }

  public Integer getCountrycode() {
    return countrycode;
  }

}
