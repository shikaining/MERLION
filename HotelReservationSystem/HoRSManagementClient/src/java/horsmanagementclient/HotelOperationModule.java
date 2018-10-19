package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.employeeAccessRightEnum;
import util.exception.DeleteRoomException;
import util.exception.DeleteRoomTypeException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class HotelOperationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public HotelOperationModule() {
    }

    public HotelOperationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuHotelOperation() throws InvalidAccessRightException, RoomNotFoundException {
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
                    
                    }  else if (response == 3) {

                        doViewAllRoomTypes();
                    } else if (response == 4) {

                        doCreateNewRoom();
                    }  else if (response == 5) {

                        doUpdateRoom();
                    }  else if (response == 6) {

                        doDeleteRoom();
                    }
                    else if (response == 7) {

                        doViewAllRooms();
                    } else if (response == 8) {

//                        doViewRoomAllocationReport();
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
                System.out.println("4: Back\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {

//                        doCreateNewRoomRate();

                    } else if (response == 2) {

//                        doViewRoomRateDetails);

                    }
                    else if (response == 3) {

//                        doViewAllRoomRates();

                    }
                   else if (response == 4) {

                        break;

                    } else {

                        System.out.println("Invalid option, please try again!\n");

                    }
                }

                if (response == 4) {

                    break;

                }
            }
        }//ends sales manager
    }//ends menu
    
    private void doCreateNewRoomType() {

        Scanner scanner = new Scanner(System.in);
        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();

        System.out.println("*** HoRS Management System :: System Administration :: Create New Room Type ***\n");
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
        List<String>amenities = new ArrayList<>();
        for (int i = 1 ; i <= numOfAmenities ; i++) {
            System.out.print("Enter Amenity "+i+"> ");
            amenity = scanner.nextLine().trim();
            amenities.add(amenity);
        }
        roomTypeEntity.setAmenities(amenities);
        //System.out.println(roomTypeEntity.getAmenities());
        roomTypeEntity = roomTypeSessionBeanRemote.createNewRoomType(roomTypeEntity);
        System.out.println("New Room Type created successfully!: " + roomTypeEntity.getRoomTypeId()+ "\n");
    }
    
    private void doViewRoomTypeDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
         
        System.out.println("*** HoRS Management System :: System Administration :: View Room Type Details ***\n");
        System.out.print("Enter Name of Room Type> ");
        String name = scanner.nextLine().trim();
        
        try
        {
            RoomTypeEntity roomTypeEntity = roomTypeSessionBeanRemote.retrieveRoomTypeByName(name);
            System.out.printf("%8s%20s%20s%15s%20s%20s%20s\n", "RoomType ID", "Name", "Description", "Size", "Bed", "Capacity", "Amenities"); 
            System.out.printf("%8s%20s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities().toString());         
            System.out.println("------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateRoomType(roomTypeEntity);
            }
            else if(response == 2)
            {
                doDeleteRoomType(roomTypeEntity);
            }
        }
        catch(RoomTypeNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving Room Type: " + ex.getMessage() + "\n");
        }
    }
    
    private void doUpdateRoomType(RoomTypeEntity roomTypeEntity) throws RoomTypeNotFoundException
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        int num;
     
        System.out.println("*** HoRS Management System :: System Administration :: Update Room Type ***\n");

        System.out.print("Enter Description of Room Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            roomTypeEntity.setDescription(input);
        }
        
        System.out.print("Enter Size of Room Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            roomTypeEntity.setSize(input);
        }
        
        System.out.print("Enter Description of Bed (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            roomTypeEntity.setBed(input);
        }
        
        System.out.print("Enter Capacity of Room Type (negative number if no change)> ");
        num = scanner.nextInt();
        if(num >= 0)
        {
            roomTypeEntity.setCapacity(num);
        }
        
        scanner.nextLine();
        
        System.out.print("Enter Number of amenities (negative number if no change)> ");
        num = scanner.nextInt();
        if(num >= 0)
        {
            scanner.nextLine();
            String amenity = "";
            List<String>amenities = new ArrayList<>();
            for (int i = 1 ; i <= num ; i++) {
                System.out.print("Enter Amenity "+i+"> ");
                amenity = scanner.nextLine().trim();
                amenities.add(amenity);
            }
        roomTypeEntity.setAmenities(amenities);
        }
                
        roomTypeSessionBeanRemote.updateRoomType(roomTypeEntity);
        System.out.println("Room Type updated successfully!\n");
    }
    
    private void doDeleteRoomType(RoomTypeEntity roomTypeEntity)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** HoRS Management System :: System Administration :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomTypeEntity.getName(), roomTypeEntity.getRoomTypeId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                roomTypeSessionBeanRemote.deleteRoomType(roomTypeEntity.getRoomTypeId());
                System.out.println("Room Type deleted successfully!\n");
            }
            catch (RoomTypeNotFoundException | DeleteRoomTypeException ex) 
            {
                System.out.println("An error has occurred while deleting room type: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Room Type NOT deleted!\n");
        }
    }
    
    private void doViewAllRoomTypes()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** HoRS Management System :: System Administration :: View All Room Types ***\n");
        
        List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
        System.out.printf("%8s%20s%20s%15s%20s%20s%20s\n", "RoomType ID", "Name", "Description", "Size", "Bed", "Capacity", "Amenities"); 

        for(RoomTypeEntity roomTypeEntity:roomTypeEntities)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s%20s\n", roomTypeEntity.getRoomTypeId().toString(), roomTypeEntity.getName(), roomTypeEntity.getDescription(), roomTypeEntity.getSize(), roomTypeEntity.getBed(), roomTypeEntity.getCapacity(), roomTypeEntity.getAmenities().toString());         
              
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    private void doCreateNewRoom() {

        Scanner scanner = new Scanner(System.in);
        RoomEntity roomEntity = new RoomEntity();

        System.out.println("*** HoRS Management System :: System Administration :: Create New Room ***\n");
        System.out.print("Enter Room Number> ");
        roomEntity.setRoomNumber(scanner.nextLine().trim());
        roomEntity.setAvailable(Boolean.TRUE);
        
        while (true) {
            System.out.print("Select Room Type> ");
            List <RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
            int numRoomTypes = roomTypeEntities.size();
            for (int i = 1 ; i <= numRoomTypes ; i++) {
                System.out.println(i + ": " + roomTypeEntities.get(i-1));
            }
            Integer roomTypeInt = scanner.nextInt();

            if (roomTypeInt >= 1 && roomTypeInt <= numRoomTypes) {
                roomEntity.setRoomTypeEntity(roomTypeEntities.get(roomTypeInt-1));

                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }

        scanner.nextLine();
        
        roomEntity = roomSessionBeanRemote.createNewRoom(roomEntity);
        System.out.println("New Room created successfully!: " + roomEntity.getRoomId()+ "\n");
    }
    
     private void doUpdateRoom() throws RoomNotFoundException
    {
        Scanner scanner = new Scanner(System.in);        
        String roomNumber;
        String available;
     
        System.out.println("*** HoRS Management System :: System Administration :: Update Room ***\n");

        System.out.print("Enter Room Number of Room to be Updated> ");
        roomNumber = scanner.nextLine().trim();
        
        RoomEntity roomEntity = new RoomEntity();
        roomEntity = roomSessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);
        
        System.out.print("Enter New Room Number(blank if no change)> ");
        roomNumber = scanner.nextLine().trim();
        if(roomNumber.length() > 0)
        {
            roomEntity.setRoomNumber(roomNumber);
        }
        
        System.out.print("Enter Availability of Room ('Y' for Available, 'N' for Occupied)> ");
        available = scanner.nextLine().trim();
        if(available.equals('Y')) {
            roomEntity.setAvailable(Boolean.TRUE);
        } else if(available.equals('N')) {
            roomEntity.setAvailable(Boolean.FALSE);
        }
        
        while (true) {
            System.out.print("Select Room Type> ");
            List <RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanRemote.retrieveAllRoomTypes();
            int numRoomTypes = roomTypeEntities.size();
            for (int i = 1 ; i <= numRoomTypes ; i++) {
                System.out.println(i + ": " + roomTypeEntities.get(i-1));
            }
            Integer roomTypeInt = scanner.nextInt();

            if (roomTypeInt >= 1 && roomTypeInt <= numRoomTypes) {
                roomEntity.setRoomTypeEntity(roomTypeEntities.get(roomTypeInt-1));

                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }
        roomSessionBeanRemote.updateRoom(roomEntity);
        System.out.println("Room updated successfully!\n");
    }
     
     private void doDeleteRoom() throws RoomNotFoundException
    {
        Scanner scanner = new Scanner(System.in);        
        String roomNumber;
        
        System.out.println("*** HoRS Management System :: System Administration :: Delete Room ***\n");
        System.out.print("Enter Room Number of Room to be Deleted> ");
        roomNumber = scanner.nextLine().trim();
        
        RoomEntity roomEntity = new RoomEntity();
        roomEntity = roomSessionBeanRemote.retrieveRoomByRoomNumber(roomNumber);
        
        if (roomEntity.getAvailable() == Boolean.TRUE) {
            
        System.out.printf("Confirm Delete Room Number %s (Room ID: %d) (Enter 'Y' to Delete)> ", roomEntity.getRoomNumber(), roomEntity.getRoomId());
        String input = scanner.nextLine().trim();
        
            if(input.equals("Y"))
            {
                try 
                {
                    roomSessionBeanRemote.deleteRoom(roomEntity.getRoomId());
                    System.out.println("Room deleted successfully!\n");
                }
                catch (RoomNotFoundException | DeleteRoomException ex) 
                {
                    System.out.println("An error has occurred while deleting room: " + ex.getMessage() + "\n");
                }
            }
        }
        else
        {
            System.out.println("Room NOT deleted as it is being used!\n");
        }
    }
    
    private void doViewAllRooms()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** HoRS Management System :: System Administration :: View All Rooms ***\n");
        
        List<RoomEntity> roomEntities = roomSessionBeanRemote.retrieveAllRooms();
        System.out.printf("%8s%20s%20s%15s\n", "Room ID", "Room Number", "Available", "Room Type"); 

        for(RoomEntity roomEntity:roomEntities)
        {
            System.out.printf("%8s%20s%20s%15s\n", roomEntity.getRoomId().toString(), roomEntity.getRoomNumber(), roomEntity.getAvailable(), roomEntity.getRoomTypeEntity().getName());         
              
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

}
