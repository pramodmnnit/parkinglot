package com.low.level.design.parkinglot.models.parking;

import com.low.level.design.parkinglot.exceptions.ParkingSpotException;
import com.low.level.design.parkinglot.exceptions.VehicleTypeException;
import com.low.level.design.parkinglot.models.vehicle.Vehicle;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntrancePanel {

  private String id;

  public ParkingTicket getParkingTicket(Vehicle vehicle)
      throws VehicleTypeException, ParkingSpotException {
    ParkingLot parkingLot = ParkingLot.getInstance();
    if (!parkingLot.canPark(vehicle.getType())) {
      throw new ParkingSpotException("We can't park");
    }

    ParkingSpot parkingSpot = parkingLot.getParkingSpot(vehicle.getType());
    if (Objects.isNull(parkingSpot)) {
      throw new ParkingSpotException("Parking spot not available");
    }

    return buildTicket(parkingSpot, vehicle);
  }

  public ParkingTicket getParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
    return buildTicket(parkingSpot, vehicle);
  }

  private ParkingTicket buildTicket(ParkingSpot parkingSpot, Vehicle vehicle) {
    return ParkingTicket.builder()
        .id(UUID.randomUUID().toString())
        .status(TicketStatus.ACTIVE)
        .parkingSpotId(parkingSpot.getId())
        .vehicleNumber(vehicle.getNumber())
        .issuedAt(LocalDateTime.now())
        .build();
  }

}
