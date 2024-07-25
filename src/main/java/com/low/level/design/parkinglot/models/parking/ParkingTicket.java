package com.low.level.design.parkinglot.models.parking;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingTicket {

  private String id;
  private String vehicleNumber;
  private LocalDateTime issuedAt;
  private LocalDateTime vacatedAt;
  private double charges;
  private TicketStatus status;
  private String parkingSpotId;

}
