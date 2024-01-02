package com.low.level.design.parkinglot.models.vehicle;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Vehicle {

  private final VehicleType type;
  private String number;

  protected Vehicle(VehicleType type, String number) {
    this.type = type;
    this.number = number;
  }

}
