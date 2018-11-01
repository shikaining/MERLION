package ejb.session.stateless;

import entity.RoomTypeEntity;
import util.exception.RoomTypeNotFoundException;

public interface RoomTypeSessionBeanLocal {

    RoomTypeEntity retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException;
    RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity);
}
