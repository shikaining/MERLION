package horsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class MainApp {

    private GuestSessionBeanRemote guestSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestEntity currentGuest;

    public MainApp() {
    }

    public MainApp(GuestSessionBeanRemote guestSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this();
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }

    public void runApp() throws RoomNotFoundException, ParseException, GuestNotFoundException, RoomTypeNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Reservation System ***\n");

            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentGuest.getFirstName() + " " + currentGuest.getLastName() + "!\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    doRegister();
                } else if (response == 3) {
                    doSearchRoom();
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

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** HoRS Management System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {

            currentGuest = guestSessionBeanRemote.guestLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() throws RoomNotFoundException, ParseException, GuestNotFoundException, RoomTypeNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Reservation System ***\n");
            System.out.println("You are login as " + currentGuest.getFirstName() + " " + currentGuest.getLastName() + "\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservation");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchRoom();
                } else if (response == 2) {
                    doViewMyReservations();
                } else if (response == 3) {
                    doViewAllReservations();
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

    private void doRegister() throws GuestNotFoundException {

        String statusMsg = "";
        Scanner scanner = new Scanner(System.in);
        GuestEntity guestEntity = new GuestEntity();

        System.out.println("*** HoRS Reservation System :: Register ***\n");
        System.out.print("Enter Identification Number> ");
        String identificationNumber = scanner.nextLine().trim();
        guestEntity = guestSessionBeanRemote.retrieveGuestByID(identificationNumber);

        if (guestEntity.getIdentificationNumber() != null) {
            if (guestEntity.getUserName() == "" || guestEntity.getUserName() == null) {
                System.out.print("Enter Username> ");
                guestEntity.setUserName(scanner.nextLine().trim());
                System.out.print("Enter Password> ");
                guestEntity.setPassword(scanner.nextLine().trim());

                guestEntity = guestSessionBeanRemote.addUsername(guestEntity);
                statusMsg = "Registered successfully!: " + guestEntity.getGuestId() + "\n";
            } else {
                statusMsg = "Account already registered!";
            }
        } else {
            guestEntity.setIdentificationNumber(identificationNumber);
            System.out.print("Enter First Name> ");
            guestEntity.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Last Name> ");
            guestEntity.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Email> ");
            guestEntity.setEmail(scanner.nextLine().trim());
            System.out.print("Enter Username> ");
            guestEntity.setUserName(scanner.nextLine().trim());
            System.out.print("Enter Password> ");
            guestEntity.setPassword(scanner.nextLine().trim());

            guestEntity = guestSessionBeanRemote.createNewGuest(guestEntity);
            statusMsg = "Registered successfully!: " + guestEntity.getGuestId() + "\n";
        }

        System.out.println(statusMsg);
    }

    private void doSearchRoom() throws ParseException, GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException, RoomTypeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date checkInDate;
        Date checkOutDate;

        System.out.println("*** HoRS Reservation System :: Search Room***\n");
        System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
        checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
        System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
        checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());

        List<RoomTypeEntity> availableRoomTypes = roomTypeSessionBeanRemote.retrieveAvailableRoomTypes(checkInDate, checkOutDate);
        System.out.printf("%3s%15s%15s%15s\n", "S/N", "Room Type", "Quantity", "Amount");

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
                BigDecimal amount = reservationSessionBeanRemote.calculateAmount(roomTypeEntity, checkInDate, checkOutDate, Boolean.TRUE);
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
                    if (currentGuest != null) {

                        doReserveRoom(availableRoomTypes, checkInDate, checkOutDate);
                    } else {
                        System.out.println("Please login first before making a reservation!\n");

                    }
                } else if (response == 2) {

                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

        }
    }

    private void doReserveRoom(List<RoomTypeEntity> availableRoomTypes, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException, RoomTypeNotFoundException {

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
                    newReservationEntity.setOnlineReservation(Boolean.TRUE);

                    GuestEntity guestEntity = new GuestEntity();
                    String identificationNumber;
                    guestId = currentGuest.getGuestId();

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            newReservationEntity = reservationSessionBeanRemote.reserve(newReservationEntity, guestId, numOfRooms, roomTypeId);
            System.out.println("Reservation Amount: " + newReservationEntity.getReservationAmount() + "\n");
            System.out.println("New Reservation created successfully!: " + newReservationEntity.getReservationId() + "\n");

        }

    }//ends reserve room

    private void doViewMyReservations() {

    }

    private void doViewAllReservations() {

    }
}
