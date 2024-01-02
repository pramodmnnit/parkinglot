package com.low.level.design.parkinglot.models.parking;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AbledParkingSpot extends ParkingSpot {

  public AbledParkingSpot(String id) {
    super(id, ParkingSpotType.ABLED);
  }

}
