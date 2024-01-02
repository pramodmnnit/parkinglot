package com.low.level.design.parkinglot.models.parking;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;


@Getter
public class Payment {

  private final String id;
  private final String ticketId;
  private final double amount;
  private final LocalDateTime initiatedAt;
  private PaymentStatus status;
  private LocalDateTime completedAt;

  public Payment(String ticketId, double amount) {
    this.id = UUID.randomUUID().toString();
    this.ticketId = ticketId;
    this.amount = amount;
    this.status = PaymentStatus.INITIATED;
    this.initiatedAt = LocalDateTime.now();
  }

  public void makePayment() {
    this.status = PaymentStatus.SUCCESS;
    this.completedAt = LocalDateTime.now();
  }

}
