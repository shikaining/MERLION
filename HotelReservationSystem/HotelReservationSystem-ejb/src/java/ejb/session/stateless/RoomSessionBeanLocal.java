package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import java.util.List;
import util.exception.RoomNotFoundException;

public interface RoomSessionBeanLocal {

    RoomEntity retrieveRoomByRoomId(Long roomId) throws RoomNotFoundException;

    RoomEntity createNewRoom(RoomEntity newRoomEntity, Long roomTypeId);

    RoomEntity retrieveRoomsByRoomId(Long roomId) throws RoomNotFoundException;

    public List<ReservationEntity> retrieveReservationsByRoomId(Long roomId);

}
