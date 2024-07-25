package com.low.level.design.parkinglot.models.parking;

public class CarParkingSpot extends ParkingSpot {

  public CarParkingSpot(String id) {
    super(id, ParkingSpotType.CAR);
  }
}
