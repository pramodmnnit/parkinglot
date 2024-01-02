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
public class Address {

  private String street;
  private String city;
  private String postalCode;
  private String state;
  private String country;

}
