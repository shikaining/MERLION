package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import java.text.ParseException;
import javax.ejb.EJB;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class Main {

    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;

    public static void main(String[] args) throws RoomNotFoundException, ParseException, GuestNotFoundException, RoomTypeNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        MainApp mainApp = new MainApp(guestSessionBeanRemote);
        mainApp.runApp();
    }

}
