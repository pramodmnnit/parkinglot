package com.low.level.design.parkinglot.models.account;


import java.util.Date;
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
public class PersonalDetails {

  private String firstName;
  private String middleName;
  private String lastName;
  private Date dateOfBirth;

}
