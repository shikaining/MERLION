package ejb.session.stateful;

import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import java.util.List;
import util.exception.ReservationNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public interface ReservationSessionBeanRemote {

    List<ReservationEntity> retrieveAllReservations();

    List<ReservedRoomEntity> retrieveAllReservedRooms();

    ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    Long linkReservedRoomToRoom(Long reservedRoomId, Long roomTypeId);

    ReservationEntity reserve(ReservationEntity newReservationEntity, Long guestId, int numOfRooms, Long roomTypeId) throws RoomTypeNotFoundException, RoomRateNotFoundException;

    List<ReservedRoomEntity> retrieveReservedRoomByGuestId(Long GuestId);

}
