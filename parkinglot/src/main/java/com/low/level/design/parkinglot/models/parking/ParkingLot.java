package com.low.level.design.parkinglot.models.parking;

import com.low.level.design.parkinglot.exceptions.ParkingSpotException;
import com.low.level.design.parkinglot.exceptions.VehicleTypeException;
import com.low.level.design.parkinglot.models.account.Address;
import com.low.level.design.parkinglot.models.vehicle.VehicleType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ParkingLot {

  @Setter
  private static ParkingLot instance;
  private String id;
  private List<ParkingFloor> floors;
  private List<EntrancePanel> entrancePanels;
  private List<ExitPanel> exitPanels;
  private Address address;

  private ParkingLot() {
    this.id = UUID.randomUUID().toString();
    this.floors = new ArrayList<>();
    this.exitPanels = new ArrayList<>();
    this.entrancePanels = new ArrayList<>();
  }

  public static ParkingLot getInstance() {
    if (Objects.isNull(instance)) {
      synchronized (ParkingLot.class) {
        instance = new ParkingLot();
      }
    }
    return instance;
  }

  public boolean isFull() {
    for (ParkingFloor each : this.floors) {
      if (!each.isFloorFull()) {
        return false;
      }
    }
    return true;
  }

  public boolean canPark(VehicleType vehicleType) throws VehicleTypeException {
    for (ParkingFloor floor : this.floors) {
      if (floor.canPark(vehicleType)) {
        return true;
      }
    }
    return false;
  }

  public ParkingSpot getParkingSpot(VehicleType vehicleType)
      throws VehicleTypeException {
    ParkingSpot parkingSpot;
    for (ParkingFloor floor : getInstance().getFloors()) {
      try {
        parkingSpot = floor.getParkingSpot(vehicleType);
        if (Objects.nonNull(parkingSpot)) {
          return parkingSpot;
        }
      } catch (ParkingSpotException ex){
        System.out.println("Error message: " + ex.getMessage());
      }
    }
    return null;
  }

  public ParkingSpot getParkingSpot(@NonNull String parkingSpotId)
      throws ParkingSpotException {
    ParkingSpot parkingSpot;
    for (ParkingFloor floor : getInstance().getFloors()) {
      parkingSpot = floor.getOccupiedParkingSpot(parkingSpotId);
      if (Objects.nonNull(parkingSpot)) {
        return parkingSpot;
      }
    }
    return null;
  }

  public void vacateParkingSpot(String parkingSpotId) {
    for (ParkingFloor floor : this.floors) {
      try {
        floor.vacateSpot(parkingSpotId);
      } catch (ParkingSpotException ex) {
        System.out.println("Exception: " + ex.getMessage());
      }
    }
  }
}
