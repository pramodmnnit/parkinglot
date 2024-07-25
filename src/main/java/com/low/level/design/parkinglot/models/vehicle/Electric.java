package com.low.level.design.parkinglot.models.vehicle;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Electric extends Vehicle {

  public Electric(String number) {
    super(VehicleType.ELECTRIC, number);
  }

}
