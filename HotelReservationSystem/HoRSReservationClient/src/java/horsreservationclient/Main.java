package horsreservationclient;

import ejb.session.stateful.ReservationSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import java.text.ParseException;
import javax.ejb.EJB;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public class Main {

//    @EJB
//    private static PartnerSessionBeanRemote partnerSessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;

    public static void main(String[] args) throws RoomNotFoundException, ParseException, GuestNotFoundException, RoomTypeNotFoundException, ReservationNotFoundException, ReservedRoomNotFoundException, RoomRateNotFoundException {
        MainApp mainApp = new MainApp(guestSessionBeanRemote, roomTypeSessionBeanRemote, roomRateSessionBeanRemote, roomSessionBeanRemote, reservationSessionBeanRemote);
        mainApp.runApp();
    }

}
