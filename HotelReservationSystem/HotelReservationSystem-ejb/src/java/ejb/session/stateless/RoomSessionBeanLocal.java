package ejb.session.stateless;

import entity.RoomEntity;
import util.exception.RoomNotFoundException;

public interface RoomSessionBeanLocal {

    RoomEntity retrieveRoomByRoomId(Long roomId) throws RoomNotFoundException;

    RoomEntity createNewRoom(RoomEntity newRoomEntity, Long roomTypeId);

    RoomEntity retrieveRoomByRoomTypeId(Long roomId) throws RoomNotFoundException;
}
