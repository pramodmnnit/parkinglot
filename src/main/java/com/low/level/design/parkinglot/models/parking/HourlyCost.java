package com.low.level.design.parkinglot.models.parking;

import java.util.HashMap;
import java.util.Map;

public class HourlyCost {

  private final Map<ParkingSpotType, Double> hourlyCots = new HashMap<>();

  public HourlyCost() {
    hourlyCots.put(ParkingSpotType.ABLED, 10.0);
    hourlyCots.put(ParkingSpotType.CAR, 30.0);
    hourlyCots.put(ParkingSpotType.EBIKE, 15.0);
    hourlyCots.put(ParkingSpotType.ELECTRIC, 20.0);
    hourlyCots.put(ParkingSpotType.LARGE, 35.0);
    hourlyCots.put(ParkingSpotType.MOTORBIKE, 25.0);
  }

  public double getCost(ParkingSpotType type) {
    return hourlyCots.get(type);
  }

}
