package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.ReportLineItemEntity;
import entity.RoomEntity;
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
import util.enumeration.rateTypeEnum;
import util.enumeration.roomStatusEnum;
import util.exception.DeleteRoomException;
import util.exception.DeleteRoomRateException;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class HotelOperationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public HotelOperationModule() {
    }

    public HotelOperationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuHotelOperation() throws InvalidAccessRightException, RoomNotFoundException, ParseException, RoomTypeNotFoundException {
        if (currentEmployee.getAccessRight() != employeeAccessRightEnum.OPERATIONMANAGER) {
            if (currentEmployee.getAccessRight() != employeeAccessRightEnum.SALESMANAGER) {
                throw new InvalidAccessRightException("You don't have OPERATIONMANAGER rights to access the Hotel Operation Module.");
            }
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        if (currentEmployee.getAccessRight() == employeeAccessRightEnum.OPERATIONMANAGER) {

            while (true) {
                System.out.println("*** HoRS Management System :: Hotel Operation ***\n");
                System.out.println("1: Create New Room Type");
                System.out.println("2: View Room Type Details");
                System.out.println("3: View All Room Types");
                System.out.println("4: Create New Room");
                System.out.println("5: Update Room");
                System.out.println("6: Delete Room");
                System.out.println("7: View All Rooms");
                System.out.println("8: View Room Allocation Exception Report");
                System.out.println("9: Back\n");
                response = 0;

                while (response < 1 || response > 9) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {

                        doCreateNewRoomType();

                    } else if (response == 2) {

                        doViewRoomTypeDetails();

                    } else if (response == 3) {

                        doViewAllRoomTypes();
                    } else if (response == 4) {

                        doCreateNewRoom();
                    } else if (response == 5) {

                        doUpdateRoom();
                    } else if (response == 6) {

                        doDeleteRoom();
                    } else if (response == 7) {

                        doViewAllRooms();
                    } else if (response == 8) {

                        doViewRoomAllocationReport();
                    } else if (response == 9) {

                        break;

                    } else {

                        System.out.println("Invalid option, please try again!\n");

                    }
                }

                if (response == 9) {

                    break;

                }
            }
        }//ends operation manager
        if (currentEmployee.getAccessRight() == employeeAccessRightEnum.SALESMANAGER) {
            while (true) {
                System.out.println("*** HoRS Management System :: Hotel Operation ***\n");
                System.out.println("1: Create New Room Rate");
                System.out.println("2: View Room Rate Details");
                System.out.println("3: View All Room Rates");
                System.out.println("4: Allocate Room");
                System.out.println("5: Back\n");
                response = 0;

                while (response < 1 || response > 5) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {

                        doCreateNewRoomRate();

                    } else if (response == 2) {

                        doViewRoomRateDetails();

                    } else if (response == 3) {

                        doViewAllRoomRates();

                    } else if (response == 4) {

                        doAllocateRoom();

                    } else if (response == 5) {

                        break;

                    } else {

                        System.out.println("Invalid option, please try again!\n");

                    }
                }

                if (response == 5) {

                    break;

                }
            }
        }//ends sales manager
    }//ends menu

    private void doCreateNewRoomType() {

        Scanner scanner = new Scanner(System.in);
        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();

        System.out.println("*** HoRS Management System :: Hotel Operation :: Create New Room Type ***\n");
        System.out.print("Enter Name> ");
        roomTypeEntity.setName(scanner.nextLine().trim());
        System.out.print("Enter Description of Room Type> ");
        roomTypeEntity.setDescription(scanner.nextLine().trim());
        System.out.print("Enter Size of Room Type> ");
        roomTypeEntity.setSize(scanner.nextLine().trim());
        System.out.print("Enter Description of Bed> ");
        roomTypeEntity.setBed(scanner.nextLine().trim());
        System.out.print("Enter Capacity of Room Type> ");
        roomTypeEntity.setCapacity(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter Number of amenities> ");
        int numOfAmenities = scanner.nextInt();
        scanner.nextLine();
        String amenity = "";
        List<String> amenities = new ArrayList<>();
        for (int i = 1; i <= numOfAmenities; i++) {
            System.out.print("Enter Amenity " + i + "> ");
            amenity = scanner.nextLine().trim();
            amenities.add(amenity);
        }
        roomTypeEntity.setAmenities(amenities);

        roomTypeEntity = roomTypeSessionBeanRemote.createNewRoomType(roomTypeEntity);
        System.out.println("New Room Type created successfully!: " + roomTypeEntity.getRoomTypeId() + "\n");
    }

    private void doViewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** HoRS Management System :: Hotel Operation :: View Room Type Details ***\n");
        System.out.print("Enter Name of Room Type> ");
        String name = scanner.nextLine().trim();

        try {
            RoomTypeEntity roomTypeEntity = roomTypeSessionBeanRemote.retrieveRoomTypeByName(name);
            System.out.printf("%10s%15s%30s%10s%30s%15s%30s\n", "RoomType ID", "Name", "Description", "Size", "Bed", "Capacity", "Amenities");
            System.out.printf("%10s%15s%30s%10s%30s%15s%30s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities().toString());
            System.out.println("------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateRoomType(roomTypeEntity);
            } else if (response == 2) {
                doDeleteRoomType(roomTypeEntity);
            }
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving Room Type: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateRoomType(RoomTypeEntity roomTypeEntity) throws RoomTypeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input;
        int num;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Update Room Type ***\n");

        System.out.print("Enter Description of Room Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            roomTypeEntity.setDescription(input);
        }

        System.out.print("Enter Size of Room Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            roomTypeEntity.setSize(input);
        }

        System.out.print("Enter Description of Bed (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            roomTypeEntity.setBed(input);
        }

        System.out.print("Enter Capacity of Room Type (negative number if no change)> ");
        num = scanner.nextInt();
        if (num >= 0) {
            roomTypeEntity.setCapacity(num);
        }

        scanner.nextLine();

        System.out.print("Enter Number of amenities (negative number if no change)> ");
        num = scanner.nextInt();
        if (num >= 0) {
            scanner.nextLine();
            String amenity = "";
            List<String> amenities = new ArrayList<>();
            for (int i = 1; i <= num; i++) {
                System.out.print("Enter Amenity " + i + "> ");
                amenity = scanner.nextLine().trim();
                amenities.add(amenity);
            }
            roomTypeEntity.setAmenities(amenities);
        }

        roomTypeSessionBeanRemote.updateRoomType(roomTypeEntity);
        System.out.println("Room Type updated successfully!\n");
    }

    private void doDeleteRoomType(RoomTypeEntity roomTypeEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomTypeEntity.getName(), roomTypeEntity.getRoomTypeId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                roomTypeSessionBeanRemote.deleteRoomType(roomTypeEntity.getRoomTypeId());
                System.out.println("Room Type deleted successfully!\n");
            } catch (RoomTypeNotFoundException | DeleteRoomTypeException ex) {
                System.out.println("An error has occurred while deleting room type: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room Type NOT deleted!\n");
        }
    }

    private void doViewAllRoomTypes() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** HoRS Management System :: Hotel Operation :: View All Room Types ***\n");

        List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%10s%15s%30s%10s%30s%15s%30s\n", "RoomType ID", "Name", "Description", "Size", "Bed", "Capacity", "Amenities");
        for (RoomTypeEntity roomTypeEntity : roomTypeEntities) {
            System.out.printf("%10s%15s%30s%10s%30s%15s%30s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities().toString());

        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewRoom() throws RoomTypeNotFoundException, RoomNotFoundException {

        Scanner scanner = new Scanner(System.in);

        RoomEntity newRoomEntity = new RoomEntity();
        Long roomId;
        Long roomTypeId;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Create New Room ***\n");
        System.out.print("Enter Room Number> ");
        newRoomEntity.setRoomNumber(scanner.nextLine().trim());
        newRoomEntity.setStatus(roomStatusEnum.AVAILABLE);

        while (true) {
            System.out.print("Select Room Type> ");
            List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
            int numRoomTypes = roomTypeEntities.size();
            System.out.print("(");
            for (int i = 1; i <= numRoomTypes; i++) {
                System.out.print(i + ": " + roomTypeEntities.get(i - 1).getName() + " ");
            }
            System.out.print(")");

            Integer roomTypeInt = scanner.nextInt();

            if (roomTypeInt >= 1 && roomTypeInt <= numRoomTypes) {
                roomTypeId = roomTypeEntities.get(roomTypeInt - 1).getRoomTypeId();
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }//ends selecting room type

        scanner.nextLine();

        newRoomEntity = roomSessionBeanRemote.createNewRoom(newRoomEntity, roomTypeId);
        System.out.println("New Room created successfully!: " + newRoomEntity.getRoomId() + "\n");
    }

    private void doUpdateRoom() throws RoomNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String roomNumber;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Update Room ***\n");

        System.out.print("Enter Room Number of Room to be Updated> ");
        roomNumber = scanner.nextLine().trim();

        RoomEntity roomEntity = new RoomEntity();
        roomEntity = roomSessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);

        System.out.print("Enter New Room Number(blank if no change)> ");
        roomNumber = scanner.nextLine().trim();
        if (roomNumber.length() > 0) {
            roomEntity.setRoomNumber(roomNumber);
        }

        System.out.print("Select Status of Room (1: Available, 2: Unavailable, 3: Cleaning, 4: Allocated)> ");
        Integer roomStatusInt = scanner.nextInt();

        if (roomStatusInt >= 1 && roomStatusInt <= 4) {
            roomEntity.setStatus(roomStatusEnum.values()[roomStatusInt - 1]);

        } else {

            System.out.println("Invalid option, please try again!\n");

        }
        scanner.nextLine().trim();

        while (true) {
            System.out.print("Select Room Type> ");
            List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
            int numRoomTypes = roomTypeEntities.size();
            System.out.print("{");
            for (int i = 1; i <= numRoomTypes; i++) {
                System.out.print(i + ": " + roomTypeEntities.get(i - 1).getName() + " ");
            }
            System.out.print(")");
            Integer roomTypeInt = scanner.nextInt();

            if (roomTypeInt >= 1 && roomTypeInt <= numRoomTypes) {
                roomEntity.setRoomTypeEntity(roomTypeEntities.get(roomTypeInt - 1));

                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }
        roomSessionBeanRemote.updateRoom(roomEntity);
        System.out.println("Room updated successfully!\n");
    }

    private void doDeleteRoom() throws RoomNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String roomNumber;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Delete Room ***\n");
        System.out.print("Enter Room Number of Room to be Deleted> ");
        roomNumber = scanner.nextLine().trim();

        RoomEntity roomEntity = new RoomEntity();
        roomEntity = roomSessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);

        if (roomEntity.getStatus() == roomStatusEnum.AVAILABLE) {

            System.out.printf("Confirm Delete Room Number %s (Room ID: %d) (Enter 'Y' to Delete)> ", roomEntity.getRoomNumber(), roomEntity.getRoomId());
            String input = scanner.nextLine().trim();

            if (input.equals("Y")) {
                try {
                    roomSessionBeanRemote.deleteRoom(roomEntity.getRoomId());
                    System.out.println("Room deleted successfully!\n");
                } catch (RoomNotFoundException | DeleteRoomException ex) {
                    System.out.println("An error has occurred while deleting room: " + ex.getMessage() + "\n");
                }
            }
        } else {
            System.out.println("Room NOT deleted as it is being used!\n");
        }
    }

    private void doViewAllRooms() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** HoRS Management System :: System Administration :: View All Rooms ***\n");

        List<RoomEntity> roomEntities = roomSessionBeanRemote.retrieveAllRooms();
        System.out.printf("%8s%15s%15s%20s\n", "Room ID", "Room Number", "Room Status", "Room Type");

        for (RoomEntity roomEntity : roomEntities) {
            System.out.printf("%8s%15s%15s%20s\n", roomEntity.getRoomId().toString(), roomEntity.getRoomNumber(), roomEntity.getStatus(), roomEntity.getRoomTypeEntity().getName());

        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewRoomRate() throws ParseException {

        Scanner scanner = new Scanner(System.in);
        RoomRateEntity roomRateEntity = new RoomRateEntity();
        Long roomTypeId;
        Long roomRateId;

        rateTypeEnum rateType = rateTypeEnum.PUBLISHED;
        System.out.println("*** HoRS Management System :: Hotel Operation :: Create New Room Rate ***\n");
        System.out.print("Enter Room Rate Name> ");
        roomRateEntity.setName(scanner.nextLine().trim());

        while (true) {
            System.out.print("Select Room Type> ");
            List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
            int numRoomTypes = roomTypeEntities.size();
            System.out.print("{");
            for (int i = 1; i <= numRoomTypes; i++) {
                System.out.print(i + ": " + roomTypeEntities.get(i - 1).getName() + " ");
            }
            System.out.print(")");
            Integer roomTypeInt = scanner.nextInt();

            if (roomTypeInt >= 1 && roomTypeInt <= numRoomTypes) {
                roomTypeId = roomTypeEntities.get(roomTypeInt - 1).getRoomTypeId();
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }

        scanner.nextLine();
        while (true) {
            System.out.print("Select Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion)> ");
            Integer rateTypeInt = scanner.nextInt();

            if (rateTypeInt >= 1 && rateTypeInt <= 4) {
                rateType = rateTypeEnum.values()[rateTypeInt - 1];
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        roomRateEntity.setRateType(rateType);

        scanner.nextLine().trim();

        BigDecimal ratePerNight;
        System.out.print("Enter Rate Per Night for New Room Rate> ");
        roomRateEntity.setRatePerNight(scanner.nextBigDecimal());

        scanner.nextLine().trim();

        System.out.print("Any Validity Period for this Room Rate? (Enter 'Y' to set Validity Period)> ");
        if (scanner.nextLine().trim().equals("Y")) {
            Date validityStart = new Date();
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            System.out.print("Enter Validity Start Date of Room Rate (dd/mm/yyyy) > ");
            validityStart = inputDateFormat.parse(scanner.nextLine().trim());
            roomRateEntity.setValidityStart(validityStart);

            Date validityEnd = new Date();
            System.out.print("Enter Validity End Date of Room Rate (dd/mm/yyyy)> ");
            validityEnd = inputDateFormat.parse(scanner.nextLine().trim());
            roomRateEntity.setValidityEnd(validityEnd);
        }
        roomRateEntity = roomRateSessionBeanRemote.createNewRoomRate(roomRateEntity, roomTypeId);
        //need to update roomTypeEntity
        System.out.println("New Room Rate created successfully!: " + roomRateEntity.getRoomRateId() + "\n");
    }

    private void doViewRoomRateDetails() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** HoRS Management System :: Hotel Operation :: View Room Rate Details ***\n");
        System.out.print("Enter Name of Room Rate> ");
        String name = scanner.nextLine().trim();

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        try {
            RoomRateEntity roomRateEntity = roomRateSessionBeanRemote.retrieveRoomRateByName(name);
            System.out.printf("%8s%20s%15s%20s%20s%25s%25s\n", "RoomRate ID", "Room Rate Name", "Rate Per Night", "Rate Type", "Room Type", "Validity Start Date", "Validity End Date");
            if (roomRateEntity.getValidityStart() != null) {
                String start = outputDateFormat.format(roomRateEntity.getValidityStart());
                String end = outputDateFormat.format(roomRateEntity.getValidityEnd());

                System.out.printf("%8s%20s%15s%20s%20s%25s%25s\n", roomRateEntity.getRoomRateId().toString(), roomRateEntity.getName(), roomRateEntity.getRatePerNight(), roomRateEntity.getRateType(), roomRateEntity.getRoomTypeEntity().getName(), start, end);
            } else {
                System.out.printf("%8s%20s%15s%20s%20s%25s%25s\n", roomRateEntity.getRoomRateId().toString(), roomRateEntity.getName(), roomRateEntity.getRatePerNight(), roomRateEntity.getRateType(), roomRateEntity.getRoomTypeEntity().getName(), roomRateEntity.getValidityStart(), roomRateEntity.getValidityEnd());

            }
            System.out.println("------------------------");
            System.out.println("1: Update Room Rate");
            System.out.println("2: Delete Room Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateRoomRate(roomRateEntity);
            } else if (response == 2) {
                doDeleteRoomRate(roomRateEntity);
            }
        } catch (RoomRateNotFoundException ex) {
            System.out.println("An error has occurred while retrieving Room Rate: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateRoomRate(RoomRateEntity roomRateEntity) throws RoomRateNotFoundException, ParseException {
        Scanner scanner = new Scanner(System.in);
        rateTypeEnum rateType = rateTypeEnum.PUBLISHED;
        String input;
        BigDecimal amount;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Update Room Rate ***\n");

        System.out.print("Enter Name of Room Rate (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            roomRateEntity.setName(input);
        }

        while (true) {
            System.out.print("Select Room Type> ");
            List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
            int numRoomTypes = roomTypeEntities.size();
            System.out.print("{");
            for (int i = 1; i <= numRoomTypes; i++) {
                System.out.print(i + ": " + roomTypeEntities.get(i - 1).getName() + " ");
            }
            System.out.print("{");
            Integer roomTypeInt = scanner.nextInt();

            if (roomTypeInt >= 1 && roomTypeInt <= numRoomTypes) {
                roomRateEntity.setRoomTypeEntity(roomTypeEntities.get(roomTypeInt - 1));

                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }
        scanner.nextLine();

        System.out.print("Select Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion)> ");
        Integer rateTypeInt = scanner.nextInt();

        if (rateTypeInt >= 1 && rateTypeInt <= 4) {
            rateType = rateTypeEnum.values()[rateTypeInt - 1];
        } else {
            System.out.println("Invalid option, please try again!\n");
        }
        roomRateEntity.setRateType(rateType);
        scanner.nextLine().trim();

        System.out.print("Enter Rate Per Night (negative number if no change)> ");
        amount = scanner.nextBigDecimal();

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            roomRateEntity.setRatePerNight(amount);
        }

        scanner.nextLine().trim();

        System.out.print("Any Validity Period for this Room Rate? (Enter 'Y' to set Validity Period)> ");
        if (scanner.nextLine().trim().equals("Y")) {
            System.out.print("Any Changes in Validity Period for this Room Rate? (Enter 'Y' to change Validity Period)> ");
            if (scanner.nextLine().trim().equals("Y")) {
                Date validityStart = new Date();
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
                System.out.print("Enter Validity Start Date of Room Rate (dd/mm/yyyy) > ");
                validityStart = inputDateFormat.parse(scanner.nextLine().trim());
                roomRateEntity.setValidityStart(validityStart);

                Date validityEnd = new Date();
                System.out.print("Enter Validity End Date of Room Rate (dd/mm/yyyy)> ");
                validityEnd = inputDateFormat.parse(scanner.nextLine().trim());
                roomRateEntity.setValidityEnd(validityEnd);
            }
        }

        roomRateSessionBeanRemote.updateRoomRate(roomRateEntity);
        System.out.println("Room Rate updated successfully!\n");
    }

    private void doDeleteRoomRate(RoomRateEntity roomRateEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** HoRS Management System :: Hotel Operation :: Delete Room Rate ***\n");
        System.out.printf("Confirm Delete Room Rate %s (Room Rate ID: %d) (Enter 'Y' to Delete)> ", roomRateEntity.getName(), roomRateEntity.getRoomRateId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                roomRateSessionBeanRemote.deleteRoomRate(roomRateEntity.getRoomRateId());
                System.out.println("Room Rate deleted successfully!\n");
            } catch (RoomRateNotFoundException | DeleteRoomRateException ex) {
                System.out.println("An error has occurred while deleting room rate: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room Rate NOT deleted!\n");
        }
    }

    private void doViewAllRoomRates() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** HoRS Management System :: Hotel Operation :: View All Room Rates ***\n");

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        List<RoomRateEntity> roomRateEntities = roomRateSessionBeanRemote.retrieveAllRoomRates();
        System.out.printf("%8s%20s%15s%20s%20s%25s%25s\n", "RoomRate ID", "Room Rate Name", "Rate Per Night", "Rate Type", "Room Type", "Validity Start Date", "Validity End Date");
        for (RoomRateEntity roomRateEntity : roomRateEntities) {
            if (roomRateEntity.getValidityStart() != null) {
                String start = outputDateFormat.format(roomRateEntity.getValidityStart());
                String end = outputDateFormat.format(roomRateEntity.getValidityEnd());

                System.out.printf("%8s%20s%15s%20s%20s%25s%25s\n", roomRateEntity.getRoomRateId().toString(), roomRateEntity.getName(), roomRateEntity.getRatePerNight(), roomRateEntity.getRateType(), roomRateEntity.getRoomTypeEntity().getName(), start, end);
            } else {
                System.out.printf("%8s%20s%15s%20s%20s%25s%25s\n", roomRateEntity.getRoomRateId().toString(), roomRateEntity.getName(), roomRateEntity.getRatePerNight(), roomRateEntity.getRateType(), roomRateEntity.getRoomTypeEntity().getName(), roomRateEntity.getValidityStart(), roomRateEntity.getValidityEnd());

            }
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewRoomAllocationReport() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** HoRS Management System :: Hotel Operation :: View Room Allocation Exception Report ***\n");
        List<ReportLineItemEntity> reportLineItemEntities = roomSessionBeanRemote.retrieveAllReportLineItems();
        System.out.printf("%10s%20s%100s\n", "Report ID", "ExceptionType", "Description");
        for (ReportLineItemEntity reportLineItemEntity : reportLineItemEntities) {
            System.out.printf("%10s%20s%100s\n", reportLineItemEntity.getReportLineItemId(), reportLineItemEntity.getTypeEnum(), reportLineItemEntity.getMessageToAdmin());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doAllocateRoom() {

        roomSessionBeanRemote.doAllocateRooms();
        System.out.println("Allocated successfully!");
    }//ends allocation

}
