package ejb.session.stateless;

import entity.RoomRateEntity;
import util.exception.RoomRateNotFoundException;

public interface RoomRateSessionBeanLocal {

    RoomRateEntity retrieveRoomRateByRoomRateId(Long roomRateId) throws RoomRateNotFoundException;
    
    RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRateEntity);
    
}
