/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.employeeAccessRightEnum;
import util.exception.InvalidAccessRightException;


public class FrontOfficeModule {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public FrontOfficeModule() {
    }
    
     public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, EmployeeEntity currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
     public void menuFrontOffice() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRight() != employeeAccessRightEnum.GUESTRELOFFICER) {
            throw new InvalidAccessRightException("You don't have GUESTRELOFFICER rights to access the Front Office Module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HoRS Management System :: Front Office ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-in Guest");
            System.out.println("5: Back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

//                    doWalkInSearchRoom();

                } else if (response == 2) {

//                    doWalkInReserveRoom();

                }
                else if (response == 3) {

//                    doCheckInGuest();

                }
                else if (response == 4) {

//                    doCheckOutGuest();

                }
                else if (response == 5) {

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
}
