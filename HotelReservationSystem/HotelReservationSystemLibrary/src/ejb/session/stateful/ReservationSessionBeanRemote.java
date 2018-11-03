package ejb.session.stateful;

import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import java.util.List;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;


public interface ReservationSessionBeanRemote {
    
    ReservationEntity createNewReservation(ReservationEntity newReservationEntity);

    List<ReservationEntity> retrieveAllReservations();

    List<ReservedRoomEntity> retrieveAllReservedRooms();
    
    void updateReservation(ReservationEntity reservationEntity) throws ReservationNotFoundException;

    ReservedNightEntity createNewReservedNight(ReservedNightEntity newReservedNightEntity);

    void updateReservedRoom(ReservedRoomEntity reservedRoomEntity) throws ReservedRoomNotFoundException;

    ReservedRoomEntity createNewReservedRoom(ReservedRoomEntity newReservedRoomEntity);
    
}
