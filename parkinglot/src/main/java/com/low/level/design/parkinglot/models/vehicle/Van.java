package com.low.level.design.parkinglot.models.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Van extends Vehicle {

  public Van(String number) {
    super(VehicleType.VAN, number);
  }

}
