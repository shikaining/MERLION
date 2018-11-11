package ejb.session.stateless;

import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.util.List;
import util.enumeration.rateTypeEnum;
import util.exception.DeleteRoomRateException;
import util.exception.RoomRateNotFoundException;

public interface RoomRateSessionBeanRemote {

    List<RoomRateEntity> retrieveAllRoomRates();

    RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRateEntity, Long roomTypeId);

    RoomRateEntity retrieveRoomRateByName(String name) throws RoomRateNotFoundException;

    void updateRoomRate(RoomRateEntity roomRateEntity) throws RoomRateNotFoundException;

    void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, DeleteRoomRateException;

    RoomRateEntity retrieveRoomRateByRoomType(RoomTypeEntity currRoomTypeEntity, rateTypeEnum rate) throws RoomRateNotFoundException;
    
}
