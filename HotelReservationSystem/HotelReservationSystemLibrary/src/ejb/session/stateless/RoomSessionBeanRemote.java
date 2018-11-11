package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import util.exception.DeleteRoomException;
import util.exception.RoomNotFoundException;

public interface RoomSessionBeanRemote {

    RoomEntity createNewRoom(RoomEntity newRoomEntity, Long roomTypeId);

    void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException;

    List<RoomEntity> retrieveAllRooms();

    void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException;

    RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public List<RoomEntity> retrieveRoomsByReservationId(Long reservationId);

    public void checkInGuest(Long roomId) throws RoomNotFoundException;

    public void checkOutGuest(Long roomId) throws RoomNotFoundException;
    
}
