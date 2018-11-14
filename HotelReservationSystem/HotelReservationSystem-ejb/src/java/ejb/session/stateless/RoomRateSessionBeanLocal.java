package ejb.session.stateless;

import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import util.enumeration.rateTypeEnum;
import util.exception.RoomRateNotFoundException;

public interface RoomRateSessionBeanLocal {

    RoomRateEntity retrieveRoomRateByRoomRateId(Long roomRateId) throws RoomRateNotFoundException;
    
    RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRateEntity);
    
    RoomRateEntity retrieveRoomRateByRoomType(RoomTypeEntity currRoomTypeEntity, rateTypeEnum rate) throws RoomRateNotFoundException;

    RoomRateEntity retrieveRoomRateByRateType(Long roomRateId, rateTypeEnum rateType) throws RoomRateNotFoundException;
    
}
