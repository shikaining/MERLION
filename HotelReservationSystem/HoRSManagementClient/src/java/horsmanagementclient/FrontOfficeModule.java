package horsmanagementclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.ReportLineItemEntity;
import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.employeeAccessRightEnum;
import util.enumeration.exceptionTypeEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class FrontOfficeModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuFrontOffice() throws InvalidAccessRightException, ParseException, GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException, RoomNotFoundException, RoomTypeNotFoundException {
        if (currentEmployee.getAccessRight() != employeeAccessRightEnum.GUESTRELOFFICER) {
            throw new InvalidAccessRightException("You don't have GUESTRELOFFICER rights to access the Front Office Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Management System :: Front Office ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doWalkInSearchRoom();

                } else if (response == 2) {

                    doCheckInGuest();
                } else if (response == 3) {

                    doCheckOutGuest();
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

    private void doWalkInSearchRoom() throws ParseException, GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException, RoomTypeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date checkInDate;
        Date checkOutDate;

        System.out.println("*** HoRS Management System :: Front Office :: Walk In Search Room***\n");
        System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
        checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
        System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
        checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

        List<RoomTypeEntity> availableRoomTypes = roomTypeSessionBeanRemote.retrieveAvailableRoomTypes(checkInDate, checkOutDate);
        System.out.printf("%3s%15s%15s%15s\n", "S/N", "Room Type", "Quantity", "Amount");
        //TODO: ADD THE RESERVATION AMOUNT FOR EACH OF THEM! THEN PASS THIS AMOUNT INTO RESERVE ROOM

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

                int nonClashes = roomTypeSessionBeanRemote.retrieveAvailableRoomCount(roomTypeEntity, checkInDate, checkOutDate);
                BigDecimal amount = reservationSessionBeanRemote.calculateAmount(roomTypeEntity.getRoomTypeId(), checkInDate, checkOutDate, false);
                ++sn;

                System.out.printf("%3s%15s%15s%15s\n", sn, roomTypeEntity.getName(), nonClashes, amount);
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

    private void doWalkInReserveRoom(List<RoomTypeEntity> availableRoomTypes, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException, RoomTypeNotFoundException {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        int numOfRooms;
        Long guestId = 1L;
        Long roomTypeId = 1L;
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
            roomTypeId = currRoomTypeEntity.getRoomTypeId();
            int nonClashes = roomTypeSessionBeanRemote.retrieveAvailableRoomCount(currRoomTypeEntity, checkInDate, checkOutDate);
            System.out.println("Enter number of rooms to reserve: ");
            numOfRooms = scanner.nextInt();
            scanner.nextLine();
            //valid number of rooms for booking

            Boolean validOption = Boolean.FALSE;

            while (!validOption) {
                if (numOfRooms <= nonClashes) {

                    validOption = Boolean.TRUE;
                    //set all the attributes of reservationEntity
                    newReservationEntity.setCheckInDate(checkInDate);
                    newReservationEntity.setCheckOutDate(checkOutDate);
                    newReservationEntity.setOnlineReservation(Boolean.FALSE);

                    GuestEntity guestEntity = new GuestEntity();
                    String identificationNumber;
                    System.out.println("Registered guest? (Enter 'Y' if guest is registered)");
                    String isGuest = scanner.nextLine().trim();

                    //FIND GUEST OR CREATE GUEST
                    if (!isGuest.equals("Y")) {

                        System.out.print("Enter identification number of guest: ");
                        guestEntity.setIdentificationNumber(scanner.nextLine().trim());
                        System.out.print("Enter first name of guest: ");
                        guestEntity.setFirstName(scanner.nextLine().trim());
                        System.out.print("Enter last name of guest: ");
                        guestEntity.setLastName(scanner.nextLine().trim());
                        System.out.print("Enter email of guest: ");
                        guestEntity.setEmail(scanner.nextLine().trim());
                        guestEntity = guestSessionBeanRemote.createNewGuest(guestEntity);

                    } else {
                        System.out.print("Enter identification number of guest: ");
                        identificationNumber = scanner.nextLine().trim();
                        guestEntity.setIdentificationNumber(identificationNumber);
                        guestEntity = guestSessionBeanRemote.retrieveGuestByID(identificationNumber);
                    }
                    guestId = guestEntity.getGuestId();

                } else {

                    System.out.println("Invalid option, please try again!\n");
                    return;

                }
            }
            newReservationEntity = reservationSessionBeanRemote.reserve(newReservationEntity, guestId, numOfRooms, roomTypeId);
            System.out.println("Reservation Amount: " + newReservationEntity.getReservationAmount() + "\n");
            System.out.println("New Reservation created successfully!: " + newReservationEntity.getReservationId() + "\n");
            doAllocateRoom(newReservationEntity.getReservationId());
        }

    }

    private void doAllocateRoom(Long reservationId) throws ReservationNotFoundException {

        //for each reserved room in reservation, link it to a room in the right room type
        ReservationEntity reservationEntity = reservationSessionBeanRemote.retrieveReservationByReservationId(reservationId);
        List<ReservedRoomEntity> reservedRoomEntities = reservationEntity.getReservedRoomEntities();

        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
            Long roomId = reservationSessionBeanRemote.linkReservedRoomToRoom(reservedRoomEntity.getReservedRoomId(), reservedRoomEntity.getRoomTypeEntity().getRoomTypeId());
        }

    }//ends allocation

    private void doCheckInGuest() throws GuestNotFoundException, RoomNotFoundException {

        Scanner scanner = new Scanner(System.in);
        String identificationNumber;
        Long reservationId;
        Long guestId;

        System.out.println("*** HoRS Management System :: Front Office :: Check In Guest***\n");
        System.out.print("Enter Guest Identificaton Number> ");
        identificationNumber = scanner.nextLine().trim();

        GuestEntity guestEntity = guestSessionBeanRemote.retrieveGuestByID(identificationNumber);
        guestId = guestEntity.getGuestId();
        List<ReservedRoomEntity> reservedRoomEntities = reservationSessionBeanRemote.retrieveReservedRoomByGuestId(guestId);
        Date currDate = new Date();
        Boolean isToday = Boolean.TRUE;

        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {

            if (isToday(reservedRoomEntity.getReservationEntity().getCheckInDate())) {
                //RETRIEVE ANY EXCEPTIONS FACED BY THIS GUEST & PRINT THE CORRESPONDING MESSAGE
                //retrieve most updated one
                List<ReportLineItemEntity> reportLineItemEntities = roomSessionBeanRemote.retrieveReportLineItemsByReservedRoomId(reservedRoomEntity.getReservedRoomId());
                //CASE 1: EXCEPTION ONE WAS MET BUT ROOM IS ALLOCATED
                if (!reportLineItemEntities.isEmpty()) {
                    if (reportLineItemEntities.get(0).getTypeEnum() == exceptionTypeEnum.EXCEPTIONONE) {
                        //if have then print the message
                        String messageToGuest = reportLineItemEntities.get(0).getMessageToGuest();
                        System.out.println(messageToGuest);
                        System.out.println("Room Number: " + reservedRoomEntity.getRoomEntity().getRoomNumber() + " has been allocated to guest.\n");
                        //update rooms
                        roomSessionBeanRemote.checkInGuest(reservedRoomEntity.getRoomEntity().getRoomId());
                    } //CASE 2: EXCEPTION TWO WAS MET AND ROOM IS NOT ALLOCATED
                    else if (reportLineItemEntities.get(0).getTypeEnum() == exceptionTypeEnum.EXCEPTIONTWO) {
                        String messageToGuest = reportLineItemEntities.get(0).getMessageToGuest();
                        System.out.println(messageToGuest);

                    }
                }//CASE 3: NO EXCEPTIONS WERE MET AND ROOM IS ALLOCATED
                else {
                    //find the allocated room and tell the guest!
                    System.out.println("Room Number: " + reservedRoomEntity.getRoomEntity().getRoomNumber() + " has been allocated to guest.\n");
                    //update rooms
                    roomSessionBeanRemote.checkInGuest(reservedRoomEntity.getRoomEntity().getRoomId());
                }
            } else {
                System.out.println("Unable to check in today! ");
            }
        }
    }//ends checkinguest

    private void doCheckOutGuest() throws GuestNotFoundException, RoomNotFoundException {

        Scanner scanner = new Scanner(System.in);
        String identificationNumber;
        Long reservationId;

        System.out.println("*** HoRS Management System :: Front Office :: Check Out Guest***\n");
        System.out.print("Enter Guest Identificaton Number> ");
        identificationNumber = scanner.nextLine().trim();

        GuestEntity guestEntity = guestSessionBeanRemote.retrieveGuestByID(identificationNumber);
        List<ReservationEntity> reservationEntities = guestEntity.getReservationEntities();
        for (ReservationEntity reservationEntity : reservationEntities) {
            List<RoomEntity> roomEntities = roomSessionBeanRemote.retrieveRoomsByReservationId(reservationEntity.getReservationId());
            for (RoomEntity roomEntity : roomEntities) {
                System.out.print("Guest has checked out of Room Number: " + roomEntity.getRoomNumber() + "\n");
                //update rooms
                roomSessionBeanRemote.checkOutGuest(roomEntity.getRoomId());
            }
        }

    }//ends checkoutguest

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

}//ends module
