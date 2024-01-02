package com.low.level.design.parkinglot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.low.level.design.parkinglot.exceptions.ParkingFloorException;
import com.low.level.design.parkinglot.exceptions.ParkingSpotException;
import com.low.level.design.parkinglot.exceptions.VehicleTypeException;
import com.low.level.design.parkinglot.models.account.Address;
import com.low.level.design.parkinglot.models.account.Admin;
import com.low.level.design.parkinglot.models.parking.CarParkingSpot;
import com.low.level.design.parkinglot.models.parking.EntrancePanel;
import com.low.level.design.parkinglot.models.parking.ExitPanel;
import com.low.level.design.parkinglot.models.parking.MotorBikeParkingSpot;
import com.low.level.design.parkinglot.models.parking.ParkingFloor;
import com.low.level.design.parkinglot.models.parking.ParkingLot;
import com.low.level.design.parkinglot.models.parking.ParkingSpot;
import com.low.level.design.parkinglot.models.parking.ParkingSpotType;
import com.low.level.design.parkinglot.models.parking.ParkingTicket;
import com.low.level.design.parkinglot.models.parking.Payment;
import com.low.level.design.parkinglot.models.parking.PaymentStatus;
import com.low.level.design.parkinglot.models.parking.TicketStatus;
import com.low.level.design.parkinglot.models.vehicle.Car;
import com.low.level.design.parkinglot.models.vehicle.MotorBike;
import com.low.level.design.parkinglot.models.vehicle.Vehicle;
import com.low.level.design.parkinglot.models.vehicle.VehicleType;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkingLotApplicationTests {

  private ParkingLot parkingLot;

  private static Address getAddress() {
    return Address.builder()
        .street("street")
        .city("city")
        .state("state")
        .country("country")
        .postalCode("pinCode")
        .build();
  }

  @BeforeEach
  void clear() {
    ParkingLot.setInstance(null);
  }

  @Test
  void parkingLotWithoutFloor() {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    assertTrue(parkingLot.isFull());
  }

  @Test
  void parkingLotWithFloors() {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    String floorId = UUID.randomUUID().toString();
    admin.addParkingFloor(new ParkingFloor(floorId));
    admin.addParkingFloor(new ParkingFloor(UUID.randomUUID().toString()));
    admin.addParkingFloor(new ParkingFloor(floorId));
    assertEquals(2, parkingLot.getFloors().size());
    assertTrue(parkingLot.getFloors().get(0).getUsedParkingSpots().isEmpty());
    assertTrue(parkingLot.getFloors().get(1).getUsedParkingSpots().isEmpty());
  }

  @Test
  void parkingLotAddingParkingSpotForInvalidParkingFloor() {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    String floorId = UUID.randomUUID().toString();
    admin.addParkingFloor(new ParkingFloor(floorId));
    ParkingSpot parkingSpot = new CarParkingSpot(UUID.randomUUID().toString());
    ParkingFloorException ex = assertThrows(ParkingFloorException.class,
        () -> admin.addParkingSpot("1", parkingSpot));
    assertEquals("Parking floor not found for floor id:1", ex.getMessage());
  }

  @Test
  void parkingLotAddingParkingSpots()
      throws ParkingFloorException, VehicleTypeException, ParkingSpotException {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    createParkingSpots(admin);

    Map<ParkingSpotType, Queue<ParkingSpot>> firstFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(0)
        .getAvailableParkingSpots();
    Map<ParkingSpotType, Queue<ParkingSpot>> secondFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(1)
        .getAvailableParkingSpots();

    ParkingSpot parkingSpot = parkingLot.getParkingSpot(VehicleType.CAR);

    assertEquals(2, parkingLot.getFloors().size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.EBIKE).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, secondFloorAvailableParkingSpots.get(ParkingSpotType.LARGE).size());
    assertFalse(parkingLot.isFull());
    assertTrue(parkingLot.canPark(VehicleType.CAR));
    assertTrue(parkingLot.canPark(VehicleType.MOTORBIKE));
    assertFalse(parkingLot.canPark(VehicleType.EBIKE));
    assertNull(parkingLot.getParkingSpot(VehicleType.ELECTRIC));
    assertNull(parkingSpot.getAssignedVehicleId());
    assertFalse(parkingSpot.isFree());
    assertEquals(ParkingSpotType.CAR, parkingSpot.getType());
    assertEquals(1, parkingLot.getFloors().get(0).getUsedParkingSpots().size());
  }

  @Test
  void removeParkingSlotsFromParkingLot()
      throws ParkingFloorException, VehicleTypeException {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    createParkingSpots(admin);

    Map<ParkingSpotType, Queue<ParkingSpot>> firstFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(0)
        .getAvailableParkingSpots();
    Map<ParkingSpotType, Queue<ParkingSpot>> secondFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(1)
        .getAvailableParkingSpots();

    assertEquals(2, parkingLot.getFloors().size());
    assertEquals(2, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.EBIKE).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, secondFloorAvailableParkingSpots.get(ParkingSpotType.LARGE).size());
    assertFalse(parkingLot.isFull());
    assertTrue(parkingLot.canPark(VehicleType.CAR));
    assertTrue(parkingLot.canPark(VehicleType.MOTORBIKE));
    assertFalse(parkingLot.canPark(VehicleType.EBIKE));

    admin.removeParkingSpot(parkingLot.getFloors().get(0).getId(), ParkingSpotType.CAR);
    admin.removeParkingSpot(parkingLot.getFloors().get(0).getId(), ParkingSpotType.MOTORBIKE);
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
  }

  @Test
  void notAbleToParkVehicleWhenSlotIsNotAvailable()
      throws ParkingFloorException, VehicleTypeException, ParkingSpotException {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    createParkingSpots(admin);

    Map<ParkingSpotType, Queue<ParkingSpot>> firstFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(0)
        .getAvailableParkingSpots();
    Map<ParkingSpotType, Queue<ParkingSpot>> secondFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(1)
        .getAvailableParkingSpots();

    assertEquals(2, parkingLot.getFloors().size());
    assertEquals(2, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.EBIKE).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, secondFloorAvailableParkingSpots.get(ParkingSpotType.LARGE).size());
    assertFalse(parkingLot.isFull());
    assertTrue(parkingLot.canPark(VehicleType.CAR));
    assertTrue(parkingLot.canPark(VehicleType.MOTORBIKE));
    assertFalse(parkingLot.canPark(VehicleType.EBIKE));

    admin.removeParkingSpot(parkingLot.getFloors().get(0).getId(), ParkingSpotType.CAR);
    admin.removeParkingSpot(parkingLot.getFloors().get(0).getId(), ParkingSpotType.MOTORBIKE);
    admin.removeParkingSpot(parkingLot.getFloors().get(1).getId(), ParkingSpotType.CAR);
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());

    Vehicle car = new Car("car1");
    Vehicle motorBike = new MotorBike("motorBike1");

    ParkingSpot carParkingSpot = parkingLot.getParkingSpot(car.getType());
    carParkingSpot.assignVehicleToSpot(car.getNumber());
    ParkingSpot motorBikeParkingSpot = parkingLot.getParkingSpot(motorBike.getType());
    motorBikeParkingSpot.assignVehicleToSpot(motorBike.getNumber());
    assertTrue(parkingLot.isFull());
    assertFalse(parkingLot.canPark(VehicleType.MOTORBIKE));
    assertNull(parkingLot.getParkingSpot(motorBike.getType()));
  }

  @Test
  void parkingLotAddingEntrancesAndExits() throws ParkingFloorException {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    createParkingSpots(admin);
    createParkingEntrances(admin);
    createParkingExits(admin);
    Map<ParkingSpotType, Queue<ParkingSpot>> firstFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(0)
        .getAvailableParkingSpots();
    Map<ParkingSpotType, Queue<ParkingSpot>> secondFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(1)
        .getAvailableParkingSpots();

    assertEquals(2, parkingLot.getFloors().size());
    assertEquals(2, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.EBIKE).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, secondFloorAvailableParkingSpots.get(ParkingSpotType.LARGE).size());
    assertEquals(2, parkingLot.getExitPanels().size());
    assertEquals(2, parkingLot.getEntrancePanels().size());
  }

  @Test
  void parkingLotParkingVehicles()
      throws ParkingFloorException, ParkingSpotException, VehicleTypeException {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    createParkingSpots(admin);
    createParkingEntrances(admin);
    createParkingExits(admin);
    Map<ParkingSpotType, Queue<ParkingSpot>> firstFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(0)
        .getAvailableParkingSpots();
    Map<ParkingSpotType, Queue<ParkingSpot>> secondFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(1)
        .getAvailableParkingSpots();

    Vehicle car = new Car("car1");
    Vehicle motorBike = new MotorBike("motorBike1");

    ParkingSpot carParkingSpot = parkingLot.getParkingSpot(car.getType());
    ParkingSpot motorBikeParkingSpot = parkingLot.getParkingSpot(motorBike.getType());
    carParkingSpot.assignVehicleToSpot(car.getNumber());
    motorBikeParkingSpot.assignVehicleToSpot(motorBike.getNumber());

    EntrancePanel entrancePanel = parkingLot.getEntrancePanels().get(0);
    ParkingTicket carParkingTicket = entrancePanel.getParkingTicket(car, carParkingSpot);
    ParkingTicket motorBikeParkingTicket = entrancePanel.getParkingTicket(motorBike,
        motorBikeParkingSpot);

    assertEquals(2, parkingLot.getFloors().size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.EBIKE).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, secondFloorAvailableParkingSpots.get(ParkingSpotType.LARGE).size());
    assertEquals(2, parkingLot.getExitPanels().size());
    assertEquals(2, parkingLot.getEntrancePanels().size());
    assertEquals("car1", carParkingSpot.getAssignedVehicleId());
    assertFalse(carParkingSpot.isFree());
    assertEquals("motorBike1", motorBikeParkingSpot.getAssignedVehicleId());
    assertFalse(motorBikeParkingSpot.isFree());
    assertEquals(2, parkingLot.getFloors().get(0).getUsedParkingSpots().size());
    assertEquals(carParkingTicket.getVehicleNumber(), car.getNumber());
    assertEquals(carParkingTicket.getParkingSpotId(),
        parkingLot.getFloors().get(0).getUsedParkingSpots().get(carParkingTicket.getParkingSpotId())
            .getId());
    assertEquals(motorBikeParkingTicket.getVehicleNumber(), motorBike.getNumber());
    assertEquals(motorBikeParkingTicket.getParkingSpotId(),
        parkingLot.getFloors().get(0).getUsedParkingSpots()
            .get(motorBikeParkingTicket.getParkingSpotId()).getId());
    assertEquals(TicketStatus.ACTIVE, motorBikeParkingTicket.getStatus());
    assertNotNull(motorBikeParkingTicket.getIssuedAt());
    assertEquals(0, motorBikeParkingTicket.getCharges());
  }

  @Test
  void parkingLotExitVehicles()
      throws ParkingFloorException, ParkingSpotException, VehicleTypeException {
    parkingLot = ParkingLot.getInstance();
    Address address = getAddress();
    parkingLot.setAddress(address);
    Admin admin = new Admin();
    createParkingSpots(admin);
    createParkingEntrances(admin);
    createParkingExits(admin);
    Map<ParkingSpotType, Queue<ParkingSpot>> firstFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(0)
        .getAvailableParkingSpots();
    Map<ParkingSpotType, Queue<ParkingSpot>> secondFloorAvailableParkingSpots = parkingLot.getFloors()
        .get(1)
        .getAvailableParkingSpots();

    Vehicle car = new Car("car1");
    Vehicle motorBike = new MotorBike("motorBike1");

    ParkingSpot carParkingSpot = parkingLot.getParkingSpot(car.getType());
    ParkingSpot motorBikeParkingSpot = parkingLot.getParkingSpot(motorBike.getType());
    carParkingSpot.assignVehicleToSpot(car.getNumber());
    motorBikeParkingSpot.assignVehicleToSpot(motorBike.getNumber());

    EntrancePanel entrancePanel = parkingLot.getEntrancePanels().get(0);
    ParkingTicket carParkingTicket = entrancePanel.getParkingTicket(car, carParkingSpot);
    ParkingTicket motorBikeParkingTicket = entrancePanel.getParkingTicket(motorBike,
        motorBikeParkingSpot);

    assertEquals(2, parkingLot.getFloors().size());
    assertEquals(1, firstFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, firstFloorAvailableParkingSpots.get(ParkingSpotType.EBIKE).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.CAR).size());
    assertEquals(1, secondFloorAvailableParkingSpots.get(ParkingSpotType.MOTORBIKE).size());
    assertEquals(0, secondFloorAvailableParkingSpots.get(ParkingSpotType.LARGE).size());
    assertEquals(2, parkingLot.getExitPanels().size());
    assertEquals(2, parkingLot.getEntrancePanels().size());
    assertEquals("car1", carParkingSpot.getAssignedVehicleId());
    assertFalse(carParkingSpot.isFree());
    assertEquals("motorBike1", motorBikeParkingSpot.getAssignedVehicleId());
    assertFalse(motorBikeParkingSpot.isFree());
    assertEquals(2, parkingLot.getFloors().get(0).getUsedParkingSpots().size());
    assertEquals(carParkingTicket.getVehicleNumber(), car.getNumber());
    assertEquals(carParkingTicket.getParkingSpotId(),
        parkingLot.getFloors().get(0).getUsedParkingSpots().get(carParkingTicket.getParkingSpotId())
            .getId());
    assertEquals(motorBikeParkingTicket.getVehicleNumber(), motorBike.getNumber());
    assertEquals(motorBikeParkingTicket.getParkingSpotId(),
        parkingLot.getFloors().get(0).getUsedParkingSpots()
            .get(motorBikeParkingTicket.getParkingSpotId()).getId());
    assertEquals(TicketStatus.ACTIVE, motorBikeParkingTicket.getStatus());
    assertNull(carParkingTicket.getVacatedAt());
    assertNotNull(motorBikeParkingTicket.getIssuedAt());
    assertEquals(0, motorBikeParkingTicket.getCharges());
    assertNull(motorBikeParkingTicket.getVacatedAt());

    ExitPanel exitPanel = parkingLot.getExitPanels().get(0);
    carParkingTicket = exitPanel.scanAndVacate(carParkingTicket);
    motorBikeParkingTicket = exitPanel.scanAndVacate(motorBikeParkingTicket);
    assertEquals(30.0, carParkingTicket.getCharges());
    assertEquals(25.0, motorBikeParkingTicket.getCharges());
    assertNotNull(carParkingTicket.getVacatedAt());
    assertNotNull(motorBikeParkingTicket.getVacatedAt());
    assertTrue(parkingLot.getFloors().get(0).getUsedParkingSpots().isEmpty());
    assertTrue(parkingLot.getFloors().get(1).getUsedParkingSpots().isEmpty());
    assertEquals(2,
        parkingLot.getFloors().get(0).getAvailableParkingSpots().get(ParkingSpotType.CAR).size());
    assertEquals(1,
        parkingLot.getFloors().get(0).getAvailableParkingSpots().get(ParkingSpotType.MOTORBIKE)
            .size());
    Payment carPayment = new Payment(carParkingTicket.getId(), carParkingTicket.getCharges());
    Payment motorBikePayment = new Payment(motorBikeParkingTicket.getId(),
        motorBikeParkingTicket.getCharges());
    assertEquals(PaymentStatus.INITIATED, carPayment.getStatus());
    assertNotNull(carPayment.getInitiatedAt());
    assertNull(carPayment.getCompletedAt());
    carPayment.makePayment();
    motorBikePayment.makePayment();
    assertNotNull(carPayment.getCompletedAt());
    assertEquals(PaymentStatus.SUCCESS, carPayment.getStatus());
    assertEquals(PaymentStatus.SUCCESS, motorBikePayment.getStatus());
  }

  private void createParkingExits(Admin admin) {
    admin.addExitPanel(new ExitPanel("1"));
    admin.addExitPanel(new ExitPanel("2"));
  }

  private void createParkingEntrances(Admin admin) {
    admin.addEntrancePanel(new EntrancePanel("1"));
    admin.addEntrancePanel(new EntrancePanel("2"));
  }

  private void createParkingSpots(Admin admin) throws ParkingFloorException {
    String firstFloorId = UUID.randomUUID().toString();
    String secondFloorId = UUID.randomUUID().toString();
    admin.addParkingFloor(new ParkingFloor(firstFloorId));
    admin.addParkingFloor(new ParkingFloor(secondFloorId));
    ParkingSpot firstFloorCarParkingSpot = new CarParkingSpot("1");
    ParkingSpot firstFloorMotorBikeParkingSpot = new MotorBikeParkingSpot("2");
    ParkingSpot secondFloorCarParkingSpot = new CarParkingSpot("3");
    ParkingSpot secondFloorMotorBikeParkingSpot = new MotorBikeParkingSpot("4");
    admin.addParkingSpot(firstFloorId, firstFloorCarParkingSpot);
    admin.addParkingSpot(firstFloorId, new CarParkingSpot("5"));
    admin.addParkingSpot(firstFloorId, firstFloorMotorBikeParkingSpot);
    admin.addParkingSpot(secondFloorId, secondFloorCarParkingSpot);
    admin.addParkingSpot(secondFloorId, secondFloorMotorBikeParkingSpot);
  }

}
