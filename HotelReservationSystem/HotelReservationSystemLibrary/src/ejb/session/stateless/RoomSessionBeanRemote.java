package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import util.exception.DeleteRoomException;
import util.exception.RoomNotFoundException;

public interface RoomSessionBeanRemote {

    RoomEntity createNewRoom(RoomEntity newRoomEntity);

    void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException;

    List<RoomEntity> retrieveAllRooms();

    void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException;

    RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;
    
}
