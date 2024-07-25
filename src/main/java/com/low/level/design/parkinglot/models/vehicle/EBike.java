package com.low.level.design.parkinglot.models.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EBike extends Vehicle {

  public EBike(String number) {
    super(VehicleType.EBIKE, number);
  }
}
