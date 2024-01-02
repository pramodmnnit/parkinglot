package com.low.level.design.parkinglot.models.account;


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
public class Contact {

  private String email;
  private String phoneNumber;
  private Address address;
  private PersonalDetails personalDetails;

}
