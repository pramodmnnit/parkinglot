package com.low.level.design.parkinglot.models.parking;

import com.low.level.design.parkinglot.exceptions.ParkingSpotException;
import com.low.level.design.parkinglot.exceptions.VehicleTypeException;
import com.low.level.design.parkinglot.models.vehicle.VehicleType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.Getter;
import lombok.NonNull;


@Getter
public class ParkingFloor {

  private final Map<ParkingSpotType, Queue<ParkingSpot>> availableParkingSpots;
  private final Map<String, ParkingSpot> usedParkingSpots = new HashMap<>();
  private final String id;

  public ParkingFloor(String floorId) {
    this.id = floorId;
    this.availableParkingSpots = Map.of(
        ParkingSpotType.CAR, new ConcurrentLinkedDeque<>(),
        ParkingSpotType.ABLED, new ConcurrentLinkedDeque<>(),
        ParkingSpotType.EBIKE, new ConcurrentLinkedDeque<>(),
        ParkingSpotType.ELECTRIC, new ConcurrentLinkedDeque<>(),
        ParkingSpotType.LARGE, new ConcurrentLinkedDeque<>(),
        ParkingSpotType.MOTORBIKE, new ConcurrentLinkedDeque<>());
  }

  public ParkingSpotType getParkingSpotTypeForVehicleType(VehicleType vehicleType)
      throws VehicleTypeException {
    switch (vehicleType) {
      case CAR -> {
        return ParkingSpotType.CAR;
      }
      case VAN -> {
        return ParkingSpotType.ABLED;
      }
      case EBIKE -> {
        return ParkingSpotType.EBIKE;
      }
      case MOTORBIKE -> {
        return ParkingSpotType.MOTORBIKE;
      }
      case TRUCK -> {
        return ParkingSpotType.LARGE;
      }
      case ELECTRIC -> {
        return ParkingSpotType.ELECTRIC;
      }
      case null, default -> throw new VehicleTypeException("Invalid vehicle type");
    }
  }

  public boolean canPark(VehicleType vehicleType) throws VehicleTypeException {
    ParkingSpotType parkingSpotType = getParkingSpotTypeForVehicleType(vehicleType);
    return !availableParkingSpots.get(parkingSpotType).isEmpty();
  }

  public boolean isFloorFull() {
    for (Map.Entry<ParkingSpotType, Queue<ParkingSpot>> each : availableParkingSpots.entrySet()) {
      if (!each.getValue().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public synchronized ParkingSpot getParkingSpot(VehicleType vehicleType)
      throws VehicleTypeException, ParkingSpotException {
    if (!canPark(vehicleType)) {
      throw new ParkingSpotException("Parking spot is not available");
    }

    ParkingSpot parkingSpot = availableParkingSpots.get(
        getParkingSpotTypeForVehicleType(vehicleType)).poll();
    if (Objects.isNull(parkingSpot)) {
      throw new ParkingSpotException("Parking spot not available");
    }
    parkingSpot.setFree(false);
    usedParkingSpots.put(parkingSpot.getId(), parkingSpot);
    return parkingSpot;
  }

  public ParkingSpot getOccupiedParkingSpot(@NonNull String parkingSpotId)
      throws ParkingSpotException {
    ParkingSpot parkingSpot = usedParkingSpots.get(parkingSpotId);
    if (Objects.isNull(parkingSpot)) {
      throw new ParkingSpotException("Parking spot not available");
    }
    return parkingSpot;
  }

  public void vacateSpot(String parkingSpotId) throws ParkingSpotException {
    ParkingSpot parkingSpot = usedParkingSpots.get(parkingSpotId);
    if (Objects.isNull(parkingSpot)) {
      throw new ParkingSpotException(
          "Parking spot not found for parking spot id: " + parkingSpotId);
    }
    parkingSpot.freeSpot();
    usedParkingSpots.remove(parkingSpotId);
    availableParkingSpots.get(parkingSpot.getType()).add(parkingSpot);
  }

}
