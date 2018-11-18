package horsmanagementclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import java.text.ParseException;
import java.util.Scanner;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private SystemAdministrationModule systemAdministrationModule;
    private HotelOperationModule hotelOperationnModule;
    private FrontOfficeModule frontOfficeModule;
    private EmployeeEntity currentEmployee;

    public MainApp() {

    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
    }

    public void runApp() throws RoomNotFoundException, ParseException, GuestNotFoundException, RoomTypeNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Management System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + "!\n");

                        systemAdministrationModule = new SystemAdministrationModule(employeeSessionBeanRemote, partnerSessionBeanRemote, currentEmployee);
                        hotelOperationnModule = new HotelOperationModule(employeeSessionBeanRemote, roomTypeSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, currentEmployee);
                        frontOfficeModule = new FrontOfficeModule(employeeSessionBeanRemote, roomTypeSessionBeanRemote, roomRateSessionBeanRemote, roomSessionBeanRemote, reservationSessionBeanRemote, guestSessionBeanRemote, currentEmployee);
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
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

            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() throws RoomNotFoundException, ParseException, GuestNotFoundException, RoomTypeNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Management System ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRight().toString() + " rights\n");
            System.out.println("1: System Administration");
            System.out.println("2: Hotel Operation");
            System.out.println("3: Front Office");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        systemAdministrationModule.menuSystemAdministration();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("You do not have the access rights!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    try {
                        hotelOperationnModule.menuHotelOperation();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("ou do not have the access rights!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    try {
                        frontOfficeModule.menuFrontOffice();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("ou do not have the access rights!: " + ex.getMessage() + "\n");
                    }
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
}
