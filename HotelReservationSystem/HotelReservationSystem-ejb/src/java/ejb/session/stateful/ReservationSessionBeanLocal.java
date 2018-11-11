package ejb.session.stateful;

import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomRateEntity;
import java.util.Date;
import java.util.List;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;

public interface ReservationSessionBeanLocal {
 
    ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    ReservedRoomEntity retrieveReservedRoomByReservedRoomId(Long reservedRoomId) throws ReservedRoomNotFoundException;

    public Long findRoomRate(List<RoomRateEntity> roomRateEntities, Date currNightDate);
    
}
