package holidayreservationsystemclient;

import ejb.session.stateful.HolidayReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import javax.ejb.EJB;



public class Main 
{
    @EJB
    private static HolidayReservationSessionBeanRemote holidayReservationSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    
    
    public static void main(String[] args)
    {
        MainApp mainApp = new MainApp(holidayReservationSessionBeanRemote, customerSessionBeanRemote);
        mainApp.runApp();
    }   
}