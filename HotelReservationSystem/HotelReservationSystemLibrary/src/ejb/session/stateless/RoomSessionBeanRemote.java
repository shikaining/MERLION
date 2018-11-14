package ejb.session.stateless;

import entity.ReportLineItemEntity;
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

    List<RoomEntity> retrieveRoomsByReservationId(Long reservationId);

    void checkInGuest(Long roomId) throws RoomNotFoundException;

    void checkOutGuest(Long roomId) throws RoomNotFoundException;

    void doAllocateRooms();

    List<ReportLineItemEntity> retrieveAllReportLineItems();

    List<ReportLineItemEntity> retrieveReportLineItemsByReservedRoomId(Long reservedRoomId);

}
