package ejb.session.stateless;

import entity.ReservedRoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import util.exception.RoomTypeNotFoundException;

public interface RoomTypeSessionBeanLocal {

    RoomTypeEntity retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException;

    RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity);

    RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException;

    public List<ReservedRoomEntity> retrieveReservedRoomsByRoomTypeName(String roomTypeName);
}
