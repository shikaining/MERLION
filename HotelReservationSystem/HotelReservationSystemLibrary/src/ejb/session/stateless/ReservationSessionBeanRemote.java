
package ejb.session.stateless;

import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import java.util.List;
import util.exception.ReservationNotFoundException;

public interface ReservationSessionBeanRemote {

    ReservationEntity createNewReservation(ReservationEntity newReservationEntity);

    List<ReservationEntity> retrieveAllReservations();

    List<ReservedRoomEntity> retrieveAllReservedRooms();

    ReservedRoomEntity createNewReservedRoom(ReservedRoomEntity newReservedRoomEntity);

    void updateReservation(ReservationEntity reservationEntity) throws ReservationNotFoundException;

    ReservedNightEntity createNewReservedNight(ReservedNightEntity newReservedNightEntity);
    
}
