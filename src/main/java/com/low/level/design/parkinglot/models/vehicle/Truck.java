package com.low.level.design.parkinglot.models.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Truck extends Vehicle {

  public Truck(String number) {
    super(VehicleType.TRUCK, number);
  }

}
