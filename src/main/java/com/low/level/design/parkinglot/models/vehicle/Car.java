package com.low.level.design.parkinglot.models.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Car extends Vehicle {

  public Car(String number) {
    super(VehicleType.CAR, number);
  }
}
