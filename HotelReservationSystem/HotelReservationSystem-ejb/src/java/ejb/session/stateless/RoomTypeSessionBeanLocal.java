package ejb.session.stateless;

import entity.ReservedRoomEntity;
import entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import util.exception.RoomTypeNotFoundException;

public interface RoomTypeSessionBeanLocal {

    RoomTypeEntity retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException;

    RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity);

    RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException;

    public List<ReservedRoomEntity> retrieveReservedRoomsByRoomTypeName(String roomTypeName);

    List<RoomTypeEntity> retrieveAllRoomTypes();

    List<RoomTypeEntity> retrieveAvailableRoomTypes(Date checkInDate, Date checkOutDate);

    Integer retrieveAvailableRoomCount(RoomTypeEntity roomTypeEntity, Date checkInDate, Date checkOutDate);

    public RoomTypeEntity retrieveRoomTypeByRank(int rank) throws RoomTypeNotFoundException;
}
