package holidayreservationsystemclient;

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
import ws.client.partnerWebService.InvalidLoginCredentialException_Exception;
import ws.client.partnerWebService.PartnerEntity;
import ws.client.partnerWebService.RoomRateNotFoundException_Exception;
import ws.client.partnerWebService.RoomTypeEntity;

public class MainApp {

    private PartnerEntity currentPartner;

    public MainApp() {
    }

    public void runApp() throws ParseException, DatatypeConfigurationException, InvalidLoginCredentialException_Exception, RoomRateNotFoundException_Exception {
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
                        System.out.println("Login successful as " + currentPartner.getName() + "!\n");
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

        System.out.println("*** HoRS Management System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {

            currentPartner = partnerLoginRemote(username, password);

        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() throws ParseException, DatatypeConfigurationException, InvalidLoginCredentialException_Exception, RoomRateNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Holiday Reservation System ***\n");
            //System.out.println("You are login as " + currentGuest.getFirstName() + " " + currentGuest.getLastName() + "\n");
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

    private void doSearchRoom() throws ParseException, DatatypeConfigurationException, InvalidLoginCredentialException_Exception, RoomRateNotFoundException_Exception {

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
        List<RoomTypeEntity> availableRoomTypes = retrieveAvailableRoomTypes(currentPartner.getUserName(), currentPartner.getPassword(), checkInDate2, checkOutDate2);
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

                //WEB METHOD
                int nonClashes = retrieveAvailableRoomCount(currentPartner.getUserName(), currentPartner.getPassword(), roomTypeEntity, checkInDate2, checkOutDate2);
                //WEB METHOD
                BigDecimal amount = calculateAmount(currentPartner.getUserName(), currentPartner.getPassword(), roomTypeEntity, checkInDate2, checkOutDate2, Boolean.TRUE);
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
//
//    private void doReserveRoom(List<RoomTypeEntity> availableRoomTypes, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException, RoomTypeNotFoundException {
//
//        Scanner scanner = new Scanner(System.in);
//        Integer response = 0;
//        int numOfRooms;
//        Long guestId = 1L;
//        Long roomTypeId = 1L;
//        ReservationEntity newReservationEntity = new ReservationEntity();
//
//        System.out.println("*** HoRS Management System :: Front Office :: Walk In Reserve Room***\n");
//        System.out.println("Select Room Type for Reservation");
//        System.out.printf("%3s%15s\n", "S/N", "Room Type");
//
//        int sn = 0;
//        //need to add room rate
//        for (RoomTypeEntity roomTypeEntity : availableRoomTypes) {
//            ++sn;
//            System.out.printf("%3s%15s\n", sn, roomTypeEntity.getName());
//        }
//
//        System.out.println("------------------------");
//
//        while (response < 1 || response > sn) {
//
//            System.out.print("> ");
//            response = scanner.nextInt();
//
//            RoomTypeEntity currRoomTypeEntity = availableRoomTypes.get(response - 1);
//            roomTypeId = currRoomTypeEntity.getRoomTypeId();
//            int nonClashes = roomTypeSessionBeanRemote.retrieveAvailableRoomCount(currRoomTypeEntity, checkInDate, checkOutDate);
//            System.out.println("Enter number of rooms to reserve: ");
//            numOfRooms = scanner.nextInt();
//            scanner.nextLine();
//            //valid number of rooms for booking
//
//            Boolean validOption = Boolean.FALSE;
//
//            while (!validOption) {
//                if (numOfRooms <= nonClashes) {
//
//                    validOption = Boolean.TRUE;
//                    //set all the attributes of reservationEntity
//                    newReservationEntity.setCheckInDate(checkInDate);
//                    newReservationEntity.setCheckOutDate(checkOutDate);
//                    newReservationEntity.setOnlineReservation(Boolean.TRUE);
//
//                    GuestEntity guestEntity = new GuestEntity();
//                    String identificationNumber;
//                    guestId = currentGuest.getGuestId();
//
//                } else {
//                    System.out.println("Invalid option, please try again!\n");
//                }
//            }
//            newReservationEntity = reservationSessionBeanRemote.reserve(newReservationEntity, guestId, numOfRooms, roomTypeId);
//            System.out.println("Reservation Amount: " + newReservationEntity.getReservationAmount() + "\n");
//            System.out.println("New Reservation created successfully!: " + newReservationEntity.getReservationId() + "\n");
//
//        }
//
//    }//ends reserve room

    private void doViewReservation() {

    }

    private void doViewAllReservations() {

    }

    public PartnerEntity partnerLoginRemote(String username, String password) {
        PartnerEntity partnerEntity = new PartnerEntity();
        try {
            return partnerLogin(username, password);
        } catch (InvalidLoginCredentialException_Exception ex) {

        }
        return partnerEntity;
    }

    private static ws.client.partnerWebService.PartnerEntity partnerLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.partnerLogin(username, password);
    }

    private static java.util.List<ws.client.partnerWebService.RoomTypeEntity> retrieveAvailableRoomTypes(java.lang.String username, java.lang.String password, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAvailableRoomTypes(username, password, checkInDate, checkOutDate);
    }

    private static BigDecimal calculateAmount(java.lang.String username, java.lang.String password, ws.client.partnerWebService.RoomTypeEntity roomTypeEntity, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate, java.lang.Boolean online) throws RoomRateNotFoundException_Exception, InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.calculateAmount(username, password, roomTypeEntity, checkInDate, checkOutDate, online);
    }

    private static Integer retrieveAvailableRoomCount(java.lang.String username, java.lang.String password, ws.client.partnerWebService.RoomTypeEntity roomTypeEntity, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAvailableRoomCount(username, password, roomTypeEntity, checkInDate, checkOutDate);
    }

}
