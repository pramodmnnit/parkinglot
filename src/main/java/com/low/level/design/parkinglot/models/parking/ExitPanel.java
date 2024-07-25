package com.low.level.design.parkinglot.models.parking;

import com.low.level.design.parkinglot.exceptions.ParkingSpotException;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExitPanel {

  private String id;

  public ParkingTicket scanAndVacate(ParkingTicket parkingTicket) throws ParkingSpotException {
    ParkingSpot parkingSpot = ParkingLot.getInstance()
        .getParkingSpot(parkingTicket.getParkingSpotId());
    parkingTicket.setCharges(calculateCost(parkingTicket, parkingSpot));
    ParkingLot.getInstance().vacateParkingSpot(parkingTicket.getParkingSpotId());
    parkingTicket.setVacatedAt(LocalDateTime.now());
    return parkingTicket;

  }

  private double calculateCost(ParkingTicket parkingTicket, ParkingSpot parkingSpot) {
    double cost = new HourlyCost().getCost(parkingSpot.getType());
    Duration duration = Duration.between(parkingTicket.getIssuedAt(), LocalDateTime.now());
    long hours = duration.toHours();
    if (hours == 0) {
      hours = 1;
    }
    return cost * hours;

  }

}
