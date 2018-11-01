/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
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
import util.enumeration.employeeAccessRightEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;

public class FrontOfficeModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuFrontOffice() throws InvalidAccessRightException, ParseException, GuestNotFoundException, ReservationNotFoundException {
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

    private void doWalkInSearchRoom() throws ParseException, GuestNotFoundException, ReservationNotFoundException {
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
            while (true) {
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

                if (response == 2) {

                    break;
                }
            }
        }
    }

    private void doWalkInReserveRoom(List<RoomTypeEntity> availableRoomTypes, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, ReservationNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        int numOfRooms = 0;
        BigDecimal reservationAmount;
        /*
        formula to find number of reserved nights
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
         */

        ReservationEntity newReservationEntity = new ReservationEntity();
        GuestEntity guestEntity = new GuestEntity();
        RoomTypeEntity currRoomTypeEntity = new RoomTypeEntity();//not null

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
            currRoomTypeEntity = availableRoomTypes.get(response - 1);
            System.out.println(currRoomTypeEntity.getName());
            int nonClashes = roomTypeSessionBeanRemote.retrieveAvailableRoomCount(currRoomTypeEntity, checkInDate, checkOutDate);
            //after choosing the room type, user keys in number of rooms available
            //user must be told of the current number of rooms available
            System.out.println("Number of available rooms: " + nonClashes);
            while (true) {

                System.out.println("Enter number of rooms to reserve: ");
                numOfRooms = scanner.nextInt();
                scanner.nextLine();
                //valid number of rooms for booking
                if (numOfRooms <= nonClashes) {
                    //set all the attributes of reservationEntity
                    newReservationEntity.setCheckInDate(checkInDate);
                    newReservationEntity.setCheckOutDate(checkOutDate);
                    newReservationEntity.setOnlineReservation(Boolean.FALSE);

                    //create new guest
                    System.out.println("Enter identification number of guest: ");
                    String identificationNumber;
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
                    guestSessionBeanRemote.createNewGuest(guestEntity);
                    newReservationEntity.setGuestEntity(guestEntity);
//                    } 

                    for (int i = 0; i < numOfRooms; i++) {

                        //for each room, create reservedRoom Entity
                        ReservedRoomEntity newReservedRoomEntity = new ReservedRoomEntity();
                        newReservedRoomEntity.setRoomTypeEntity(currRoomTypeEntity);
                        newReservedRoomEntity.setReservationEntity(newReservationEntity);
                        //default value first
                        //for each day, create reservedNight Entity
                        
                        int numOfDays = 3;
                        for (int j = 0; j < numOfDays; j++) {
                            //retrieve the right roomRate for each night
                            ReservedNightEntity reservedNightEntity = new ReservedNightEntity();
                            RoomRateEntity roomRateEntity = new RoomRateEntity();
                            //retrieve the right roomrate
                            //roomRateEntity = roomRateSessionBeanRemote.retrieveRoomRateByRoomType(currRoomTypeEntity, false);
                            reservedNightEntity.setRoomRateEntity(roomRateEntity);
                            roomRateEntity.getReservedNightEntities().add(reservedNightEntity);
                            //create the reservedNight
                            reservedNightEntity.setReservedRoomEntity(newReservedRoomEntity);
                            reservedNightEntity = reservationSessionBeanRemote.createNewReservedNight(reservedNightEntity);
                            newReservedRoomEntity.getReservedNightEntities().add(reservedNightEntity);
                        }
                        currRoomTypeEntity.getReservedRoomEntities().add(newReservedRoomEntity);
                        //create the reservedRoom
                        reservationSessionBeanRemote.createNewReservedRoom(newReservedRoomEntity);
                        //update roomType
                        //add this reservedRoom to the list of reservedRooms
                        newReservationEntity.getReservedRoomEntities().add(newReservedRoomEntity);
                    }
                } //invalid number of rooms for booking
                else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        }
        //reservationAmount
        reservationAmount = new BigDecimal("150");
        newReservationEntity.setReservationAmount(reservationAmount);
        newReservationEntity.setGuestEntity(guestEntity);
        //create the new reservation
        newReservationEntity = reservationSessionBeanRemote.createNewReservation(newReservationEntity);
        guestEntity.getReservationEntities().add(newReservationEntity);
        guestSessionBeanRemote.updateGuest(guestEntity);
        System.out.println("New Reservation created successfully!: " + newReservationEntity.getReservationId() + "\n");
    }
}//ends module
