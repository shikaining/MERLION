package ejb.session.stateful;

import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;

public interface ReservationSessionBeanLocal {
 
    ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    ReservedRoomEntity retrieveReservedRoomByReservedRoomId(Long reservedRoomId) throws ReservedRoomNotFoundException;
    
}
