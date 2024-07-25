package com.low.level.design.parkinglot.models.parking;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ParkingSpot {

  private String id;
  private boolean isFree;
  private ParkingSpotType type;
  private String assignedVehicleId;

  protected ParkingSpot(String id, ParkingSpotType type) {
    this.id = id;
    this.type = type;
    this.isFree = true;
  }

  public void assignVehicleToSpot(String vehicleId) {
    this.assignedVehicleId = vehicleId;
    this.isFree = false;
  }

  public void freeSpot() {
    this.isFree = true;
    this.assignedVehicleId = null;
  }

}
