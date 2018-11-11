package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

public interface RoomTypeSessionBeanRemote {

    RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity);

    List<RoomTypeEntity> retrieveAllRoomTypes();

    RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException;

    void updateRoomType(RoomTypeEntity roomTypeEntity) throws RoomTypeNotFoundException;

    void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException;

    List<RoomTypeEntity> retrieveAvailableRoomTypes(Date checkInDate, Date checkOutDate);

    Integer retrieveAvailableRoomCount(RoomTypeEntity roomTypeEntity, Date checkInDate, Date checkOutDate);

    void linkRoomToRoomType(Long roomId, Long roomTypeId) throws RoomTypeNotFoundException, RoomNotFoundException;
    
}
