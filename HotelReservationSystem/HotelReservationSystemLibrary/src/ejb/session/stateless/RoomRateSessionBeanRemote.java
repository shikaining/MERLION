package ejb.session.stateless;

import entity.RoomRateEntity;
import java.util.List;
import util.exception.DeleteRoomRateException;
import util.exception.RoomRateNotFoundException;

public interface RoomRateSessionBeanRemote {

    List<RoomRateEntity> retrieveAllRoomRates();

    RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRateEntity);

    RoomRateEntity retrieveRoomRateByName(String name) throws RoomRateNotFoundException;

    void updateRoomRate(RoomRateEntity roomRateEntity) throws RoomRateNotFoundException;

    void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, DeleteRoomRateException;
    
}
