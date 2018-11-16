package holidayreservationsystemclient;

import Records.PartnerRecord;
import Records.RoomTypeRecord;
import ejb.session.stateless.PartnerControllerRemote;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.InvalidLoginCredentialException;

public class MainApp {

    private PartnerRecord currentPartner;
    private PartnerControllerRemote partnerControllerRemote;

    public MainApp() {
    }

    public MainApp(PartnerControllerRemote partnerControllerRemote) {
        this.partnerControllerRemote = partnerControllerRemote;
    }

    public void runApp() throws ParseException, DatatypeConfigurationException{
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Holiday Reservation System ***\n");

            System.out.println("1: Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentPartner.getUsername() + "!\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    doSearchRoom();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Holiday Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {

            currentPartner = partnerControllerRemote.partnerLoginRemote(username, password);

        } else {
            System.out.println("Missing login credential!");
        }
    }

    private void menuMain() throws ParseException, DatatypeConfigurationException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Holiday Reservation System ***\n");
            if (currentPartner != null) {
                System.out.println("You are login as " + currentPartner.getUsername() + "\n");
                System.out.println("1: Search Hotel Room");
                System.out.println("2: View Reservation Details");
                System.out.println("3: View All Reservations");
                System.out.println("4: Logout\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doSearchRoom();
                    } else if (response == 2) {
                        doViewReservation();
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
    }

    private void doSearchRoom() throws ParseException, DatatypeConfigurationException {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date checkInDate;
        Date checkOutDate;

        GregorianCalendar calendar1 = new GregorianCalendar();

        System.out.println("*** Holiday Reservation System :: Search Room***\n");
        System.out.print("Enter Check-In Date (dd/mm/yyyy)> ");
        checkInDate = inputDateFormat.parse(scanner.nextLine().trim());
        calendar1.setTime(checkInDate);
        XMLGregorianCalendar checkInDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar1);

        GregorianCalendar calendar2 = new GregorianCalendar();
        System.out.print("Enter Check-Out Date (dd/mm/yyyy)> ");
        checkOutDate = inputDateFormat.parse(scanner.nextLine().trim());
        calendar2.setTime(checkOutDate);
        XMLGregorianCalendar checkOutDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar2);

        //WEB METHOD
        List<RoomTypeRecord> roomTypeRecords = partnerControllerRemote.retrieveAvailRoomTypesRemote(currentPartner.getUsername(), currentPartner.getPassword(), checkInDate, checkOutDate);
        System.out.printf("%3s%15s%15s%15s\n", "S/N", "Room Type", "Quantity", "Amount");

        int sn = roomTypeRecords.size();

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
            for (RoomTypeRecord roomTypeRecord : roomTypeRecords) {

                //WEB METHOD
                int nonClashes = partnerControllerRemote.roomCountRemote(currentPartner.getUsername(), currentPartner.getPassword(), roomTypeRecord, checkInDate, checkOutDate);
                //WEB METHOD
                BigDecimal amount = partnerControllerRemote.calculateAmtRemote(currentPartner.getUsername(), currentPartner.getPassword(), roomTypeRecord, checkInDate, checkOutDate, Boolean.TRUE);
                ++sn;

                System.out.printf("%3s%15s%15s%15s\n", sn, roomTypeRecord.getRoomTypeName(), nonClashes, amount);
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
                    if (currentPartner != null) {

                        //doReserveRoom(availableRoomTypes, checkInDate, checkOutDate);
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
    private void doViewReservation() {

    }

    private void doViewAllReservations() {

    }

}
