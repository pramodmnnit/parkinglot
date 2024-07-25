package com.low.level.design.parkinglot.models.account;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

  private String id;
  private String username;
  private String password;
  private LocalDateTime lastAccessed;
  private Contact contact;

}
