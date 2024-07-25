package com.low.level.design.parkinglot.models.vehicle;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotorBike extends Vehicle {

  public MotorBike(String number) {
    super(VehicleType.MOTORBIKE, number);
  }

}
