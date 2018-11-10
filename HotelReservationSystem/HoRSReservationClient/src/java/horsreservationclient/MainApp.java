package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import entity.GuestEntity;
import java.text.ParseException;
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
    private GuestEntity currentGuest;

    public MainApp() {
    }

    public MainApp(GuestSessionBeanRemote guestSessionBeanRemote) {
        this();
        this.guestSessionBeanRemote = guestSessionBeanRemote;
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
                    //Search Hotel Room
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

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    //Reserve Hotel Room
                } else if (response == 2) {
                    //View Reservation Details
                } else if (response == 3) {
                    //View All Reservation
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
}
