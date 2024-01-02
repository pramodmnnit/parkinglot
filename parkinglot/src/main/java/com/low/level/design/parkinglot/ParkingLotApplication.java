package com.low.level.design.parkinglot;


import com.low.level.design.parkinglot.models.account.Address;
import com.low.level.design.parkinglot.models.parking.ParkingLot;

public class ParkingLotApplication {

  public static void main(String[] args) {
    ParkingLot parkingLot = ParkingLot.getInstance();
    Address address = Address.builder()
        .street("xyz ")
        .city("city")
        .state("state")
        .country("country")
        .postalCode("123456")
        .build();
    parkingLot.setAddress(address);
    System.out.println("Is parking lot full:" + parkingLot.isFull());
  }

}
