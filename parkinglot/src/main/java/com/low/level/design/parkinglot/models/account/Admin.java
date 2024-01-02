package com.low.level.design.parkinglot.models.account;


import com.low.level.design.parkinglot.exceptions.ParkingFloorException;
import com.low.level.design.parkinglot.models.parking.EntrancePanel;
import com.low.level.design.parkinglot.models.parking.ExitPanel;
import com.low.level.design.parkinglot.models.parking.ParkingFloor;
import com.low.level.design.parkinglot.models.parking.ParkingLot;
import com.low.level.design.parkinglot.models.parking.ParkingSpot;
import com.low.level.design.parkinglot.models.parking.ParkingSpotType;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Admin extends Account {

  public void addParkingFloor(ParkingFloor parkingFloor) {
    ParkingLot parkingLot = ParkingLot.getInstance();
    Optional<ParkingFloor> parkingFloorOptional = parkingLot.getFloors().stream()
        .filter(each -> each.getId().equalsIgnoreCase(parkingFloor.getId()))
        .findFirst();
    if (parkingFloorOptional.isPresent()) {
      return;
    }
    parkingLot.getFloors().add(parkingFloor);

  }

  public void addParkingSpot(String floorId, ParkingSpot parkingSpot) throws ParkingFloorException {
    ParkingLot parkingLot = ParkingLot.getInstance();
    Optional<ParkingFloor> parkingFloorOptional = parkingLot.getFloors().stream()
        .filter(each -> each.getId().equalsIgnoreCase(floorId))
        .findFirst();
    if (parkingFloorOptional.isEmpty()) {
      throw new ParkingFloorException("Parking floor not found for floor id:" + floorId);
    }
    Optional<ParkingSpot> parkingSpotOptional = parkingFloorOptional.get()
        .getAvailableParkingSpots()
        .get(parkingSpot.getType())
        .stream()
        .filter(each -> each.getId().equalsIgnoreCase(parkingSpot.getId()))
        .findFirst();
    if (parkingSpotOptional.isPresent()) {
      System.out.println("Parking spot is already present");
      return;
    }
    parkingFloorOptional.get().getAvailableParkingSpots().get(parkingSpot.getType())
        .add(parkingSpot);
  }

  public void removeParkingSpot(String floorId, ParkingSpotType parkingSpotType)
      throws ParkingFloorException {
    ParkingLot parkingLot = ParkingLot.getInstance();
    Optional<ParkingFloor> parkingFloorOptional = parkingLot.getFloors().stream()
        .filter(each -> each.getId().equalsIgnoreCase(floorId))
        .findFirst();
    if (parkingFloorOptional.isEmpty()) {
      throw new ParkingFloorException("Parking floor not found for floor id:" + floorId);
    }
    Optional<ParkingSpot> parkingSpotOptional = parkingFloorOptional.get()
        .getAvailableParkingSpots()
        .get(parkingSpotType)
        .stream()
        .findFirst();
    if (parkingSpotOptional.isEmpty()) {
      System.out.println("Parking spot not found");
      return;
    }
    parkingFloorOptional.get().getAvailableParkingSpots().get(parkingSpotType).remove();
  }

  public void addEntrancePanel(EntrancePanel entrancePanel) {
    ParkingLot parkingLot = ParkingLot.getInstance();
    Optional<EntrancePanel> entrancePanelOptional = parkingLot.getEntrancePanels().stream()
        .filter(each -> each.getId().equalsIgnoreCase(entrancePanel.getId()))
        .findFirst();
    if (entrancePanelOptional.isPresent()) {
      System.out.println("Entrance panel is already present");
      return;
    }
    parkingLot.getEntrancePanels().add(entrancePanel);
  }

  public void addExitPanel(ExitPanel exitPanel) {
    ParkingLot parkingLot = ParkingLot.getInstance();
    Optional<ExitPanel> exitPanelOptional = parkingLot.getExitPanels()
        .stream()
        .filter(each -> each.getId().equalsIgnoreCase(exitPanel.getId()))
        .findFirst();

    if (exitPanelOptional.isPresent()) {
      System.out.println("Exit panel is already present");
      return;
    }
    parkingLot.getExitPanels().add(exitPanel);
  }
}
