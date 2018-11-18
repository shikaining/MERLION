package ejb.session.stateful;

import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomRateEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

public interface ReservationSessionBeanLocal {

    ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    ReservedRoomEntity retrieveReservedRoomByReservedRoomId(Long reservedRoomId) throws ReservedRoomNotFoundException;

    public Long findRoomRate(List<RoomRateEntity> roomRateEntities, Date currNightDate);

    Long linkReservedRoomToRoom(Long reservedRoomId, Long roomTypeId);

    BigDecimal calculateAmount(Long roomTypeId, Date checkInDate, Date checkOutDate, Boolean online) throws RoomRateNotFoundException;

    Long reserveForPartner(ReservationEntity newReservationEntity, Long partnerId, int numOfRooms, Long roomTypeId) throws RoomTypeNotFoundException, RoomRateNotFoundException;

    List<ReservedRoomEntity> retrieveReservedRoomsByReservationId(Long reservationId);

    List<ReservedRoomEntity> retrieveReservedRoomByPartnerId(Long partnerId);
}
