package horsmanagementclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import util.enumeration.employeeAccessRightEnum;
import util.enumeration.rateTypeEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomRateNotFoundException;

public class FrontOfficeModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuFrontOffice() throws InvalidAccessRightException, ParseException, GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        if (currentEmployee.getAccessRight() != employeeAccessRightEnum.GUESTRELOFFICER) {
            throw new InvalidAccessRightException("You don't have GUESTRELOFFICER rights to access the Front Office Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Management System :: Front Office ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doWalkInSearchRoom();

                } else if (response == 2) {

//                    doCheckInGuest();
                } else if (response == 3) {

//                    doCheckOutGuest();
                } else if (response == 4) {

                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

            if (response == 4) {

                break;

            }
        }
    }

    private void doWalkInSearchRoom() throws ParseException, GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date checkInDate;
        Date checkOutDate;

        System.out.println("*** HoRS Management System :: Front Office :: Walk In Search Room***\n");
        System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
        checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
        System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
        checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

        List<RoomTypeEntity> availableRoomTypes = roomTypeSessionBeanRemote.retrieveAvailableRoomTypes(checkInDate, checkOutDate);
        System.out.printf("%3s%15s\n", "S/N", "Room Type");

        int sn = availableRoomTypes.size();

        if (sn == 0) {
            System.out.println("No Room Types are available for the given dates");
            System.out.println("1: Back\n");
            response = scanner.nextInt();
            while (response < 1 || response > 2) {
                System.out.print("> ");
                if (response == 1) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }

                if (response == 1) {
                    break;
                }
            }
        } else {
            sn = 0;
            for (RoomTypeEntity roomTypeEntity : availableRoomTypes) {
                ++sn;

                System.out.printf("%3s%15s\n", sn, roomTypeEntity.getName());
            }

            System.out.println("------------------------");
            System.out.println("1: Make Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doWalkInReserveRoom(availableRoomTypes, checkInDate, checkOutDate);

                } else if (response == 2) {

                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

        }
    }

    private void doWalkInReserveRoom(List<RoomTypeEntity> availableRoomTypes, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        int numOfRooms = 0;
        BigDecimal reservationAmount;
        ReservationEntity newReservationEntity = new ReservationEntity();

        System.out.println("*** HoRS Management System :: Front Office :: Walk In Reserve Room***\n");
        System.out.println("Select Room Type for Reservation");
        System.out.printf("%3s%15s\n", "S/N", "Room Type");

        int sn = 0;
        //need to add room rate
        for (RoomTypeEntity roomTypeEntity : availableRoomTypes) {
            ++sn;
            System.out.printf("%3s%15s\n", sn, roomTypeEntity.getName());
        }

        System.out.println("------------------------");

        while (response < 1 || response > sn) {

            System.out.print("> ");
            response = scanner.nextInt();
            RoomTypeEntity currRoomTypeEntity = availableRoomTypes.get(response - 1);

            int nonClashes = roomTypeSessionBeanRemote.retrieveAvailableRoomCount(currRoomTypeEntity, checkInDate, checkOutDate);
            System.out.println("Number of available rooms: " + nonClashes);
            System.out.println("Enter number of rooms to reserve: ");
            numOfRooms = scanner.nextInt();
            scanner.nextLine();
            //valid number of rooms for booking
            if (numOfRooms <= nonClashes) {

                //set all the attributes of reservationEntity
                newReservationEntity.setCheckInDate(checkInDate);
                newReservationEntity.setCheckOutDate(checkOutDate);
                newReservationEntity.setOnlineReservation(Boolean.FALSE);

                //RESERVED ROOMS
                List<ReservedRoomEntity> reservedRoomEntities = new ArrayList<>();
                newReservationEntity = reservationSessionBeanRemote.createNewReservation(newReservationEntity);
                //for each room, create reservedRoom Entity
                for (int i = 0; i < numOfRooms; i++) {

                    long diff = checkOutDate.getTime() - checkInDate.getTime();
                    int numNights = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                    ReservedRoomEntity newReservedRoomEntity = doCreateNewReservedRoom(newReservationEntity, currRoomTypeEntity, numNights);
                    reservedRoomEntities.add(newReservedRoomEntity);
                    currRoomTypeEntity.getReservedRoomEntities().add(newReservedRoomEntity);
                }
                newReservationEntity.setReservedRoomEntities(reservedRoomEntities);
            } //invalid number of rooms for booking
            else {
                System.out.println("Invalid option, please try again!\n");
            }
        }//ends choosing of room type
        GuestEntity guestEntity = doCreateNewGuest(newReservationEntity);
        System.out.println(guestEntity.getGuestId());
        newReservationEntity.setGuestEntity(guestEntity);
        reservationSessionBeanRemote.updateReservation(newReservationEntity);
        System.out.println("New Reservation created successfully!: " + newReservationEntity.getReservationId() + "\n");
    }

    private GuestEntity doCreateNewGuest(ReservationEntity reservationEntity) {

        Scanner scanner = new Scanner(System.in);
        GuestEntity guestEntity = new GuestEntity();
        String identificationNumber;

        System.out.println("Enter identification number of guest: ");
        identificationNumber = scanner.nextLine().trim();

//                  assume is a new guest
//                  guestEntity = guestSessionBeanRemote.retrieveGuestByID(identificationNumber);
//                  if (guestEntity == null) {
        guestEntity.setIdentificationNumber(identificationNumber);
        System.out.println("Enter first name of guest: ");
        guestEntity.setFirstName(scanner.nextLine().trim());
        System.out.println("Enter last name of guest: ");
        guestEntity.setLastName(scanner.nextLine().trim());
        System.out.println("Enter email of guest: ");
        guestEntity.setEmail(scanner.nextLine().trim());
        List<ReservationEntity> reservationEntities = new ArrayList<>();
        reservationEntities.add(reservationEntity);
        guestEntity.setReservationEntities(reservationEntities);
        guestEntity = guestSessionBeanRemote.createNewGuest(guestEntity);
        return guestEntity;
    }

    private ReservedRoomEntity doCreateNewReservedRoom(ReservationEntity reservationEntity, RoomTypeEntity roomTypeEntity, int numReservedNights) throws RoomRateNotFoundException, ReservedRoomNotFoundException {

        Scanner scanner = new Scanner(System.in);
        ReservedRoomEntity reservedRoomEntity = new ReservedRoomEntity();
        reservedRoomEntity.setRoomTypeEntity(roomTypeEntity);
        reservedRoomEntity.setReservationEntity(reservationEntity);
        reservedRoomEntity = reservationSessionBeanRemote.createNewReservedRoom(reservedRoomEntity);
        List<ReservedNightEntity> reservedNightEntities = new ArrayList<>();
        for (int i = 0; i < numReservedNights; i++) {
            ReservedNightEntity reservedNightEntity = new ReservedNightEntity();
            reservedNightEntity = doCreateNewReservedNight(reservedRoomEntity);
            reservedNightEntities.add(reservedNightEntity);
        }
        reservedRoomEntity.setReservedNightEntities(reservedNightEntities);
        reservationSessionBeanRemote.updateReservedRoom(reservedRoomEntity);
        return reservedRoomEntity;
    }

    private ReservedNightEntity doCreateNewReservedNight(ReservedRoomEntity reservedRoomEntity) throws RoomRateNotFoundException {

        ReservedNightEntity reservedNightEntity = new ReservedNightEntity();
        reservedNightEntity.setReservedRoomEntity(reservedRoomEntity);
        //retrieve the right roomrate
        rateTypeEnum currRateTypeEnum = rateTypeEnum.PUBLISHED;
        RoomRateEntity roomRateEntity = roomRateSessionBeanRemote.retrieveRoomRateByRoomType(reservedRoomEntity.getRoomTypeEntity(), currRateTypeEnum);
        reservedNightEntity.setRoomRateEntity(roomRateEntity);
        reservedNightEntity.setAmount(roomRateEntity.getRatePerNight());
        //create the reservedNight
        reservedNightEntity = reservationSessionBeanRemote.createNewReservedNight(reservedNightEntity);
        roomRateEntity.getReservedNightEntities().add(reservedNightEntity);
        System.out.println("added reservednightentity to roomrate");
        roomRateSessionBeanRemote.updateRoomRate(roomRateEntity);
        return reservedNightEntity;
    }
}//ends module
