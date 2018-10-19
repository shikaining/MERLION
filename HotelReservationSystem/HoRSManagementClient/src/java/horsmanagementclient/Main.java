package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.RoomNotFoundException;



public class Main 
{
    
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    
    
    public static void main(String[] args) throws RoomNotFoundException
    {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, partnerSessionBeanRemote, roomTypeSessionBeanRemote, roomSessionBeanRemote);
        mainApp.runApp();
    }   
}