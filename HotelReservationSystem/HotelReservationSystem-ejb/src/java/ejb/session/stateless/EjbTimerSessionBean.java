package ejb.session.stateless;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;
    
    @Schedule(hour = "2", info = "allocateRoomsCheckTimer")
    public void allocateRoomsCheckTimer() {
        
        roomSessionBeanLocal.doAllocateRooms();
    }
    
}
