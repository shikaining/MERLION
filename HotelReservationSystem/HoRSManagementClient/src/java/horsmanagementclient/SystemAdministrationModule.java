package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.employeeAccessRightEnum;
import util.exception.InvalidAccessRightException;

public class SystemAdministrationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuSystemAdministration() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != employeeAccessRightEnum.SYSTEMADMIN) {
            throw new InvalidAccessRightException("You don't have SYSTEMADMIN rights to access the System Administration Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Management System :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    doCreateNewEmployee();

                } else if (response == 2) {

                    doViewAllEmployees();

                }
                else if (response == 3) {

                    doCreateNewPartner();

                }
                else if (response == 4) {

                    doViewAllPartners();

                }else if (response == 5) {

                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

            if (response == 5) {

                break;

            }
        }
    }

    private void doCreateNewEmployee() {

        Scanner scanner = new Scanner(System.in);
        EmployeeEntity employeeEntity = new EmployeeEntity();

        System.out.println("*** HoRS Management System :: System Administration :: Create New Employee ***\n");
        System.out.print("Enter First Name> ");
        employeeEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        employeeEntity.setLastName(scanner.nextLine().trim());

        while (true) {
            System.out.print("Select Access Right (1: System Administrator, 2: Operation Manager, 3: Sales Manager, 4: Guest Relation Officer)> ");
            Integer accessRightInt = scanner.nextInt();

            if (accessRightInt >= 1 && accessRightInt <= 4) {
                employeeEntity.setAccessRight(employeeAccessRightEnum.values()[accessRightInt - 1]);

                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }

        scanner.nextLine();

        System.out.print("Enter Username> ");
        employeeEntity.setUserName(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        employeeEntity.setPassword(scanner.nextLine().trim());

        employeeEntity = employeeSessionBeanRemote.createNewEmployee(employeeEntity);
        System.out.println("New Employee created successfully!: " + employeeEntity.getEmployeeId() + "\n");
    }

    private void doViewAllEmployees() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** HoRS Management System :: System Administration :: View All Employees ***\n");

        List<EmployeeEntity> employeeEntities = employeeSessionBeanRemote.retrieveAllEmployees();
        System.out.printf("%8s%20s%20s%20s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for (EmployeeEntity employeeEntity : employeeEntities) {
            System.out.printf("%8s%20s%20s%20s%20s%20s\n", employeeEntity.getEmployeeId().toString(), employeeEntity.getFirstName(), employeeEntity.getLastName(), employeeEntity.getAccessRight().toString(), employeeEntity.getUserName(), employeeEntity.getPassword());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
     private void doCreateNewPartner() {

        Scanner scanner = new Scanner(System.in);
         PartnerEntity partnerEntity = new PartnerEntity();

        System.out.println("*** HoRS Management System :: System Administration :: Create New Partner ***\n");
        System.out.print("Enter Partner Name> ");
        partnerEntity.setName(scanner.nextLine().trim());

        System.out.print("Enter Username> ");
        partnerEntity.setUserName(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        partnerEntity.setPassword(scanner.nextLine().trim());

        partnerEntity = partnerSessionBeanRemote.createNewPartner(partnerEntity);
        System.out.println("New Partner created successfully!: " + partnerEntity.getPartnerId()+ "\n");
    }
     
      private void doViewAllPartners() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** HoRS Management System :: System Administration :: View All Partners ***\n");

        List<PartnerEntity> partnerEntities = partnerSessionBeanRemote.retrieveAllPartners();
        System.out.printf("%8s%20s%20s%20s\n", "Partner ID", "Name", "Username", "Password");

        for (PartnerEntity partnerEntity : partnerEntities) {
            System.out.printf("%8s%20s%20s%20s\n", partnerEntity.getPartnerId().toString(), partnerEntity.getName(), partnerEntity.getUserName(), partnerEntity.getPassword());
         
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
