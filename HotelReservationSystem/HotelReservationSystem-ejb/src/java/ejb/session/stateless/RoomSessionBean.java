package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.roomStatusEnum;
import util.exception.DeleteRoomException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

@Stateless
@Local(RoomSessionBeanLocal.class)
@Remote(RoomSessionBeanRemote.class)
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomEntity createNewRoom(RoomEntity newRoomEntity, Long roomTypeId) {

        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
        
        try {
            roomTypeEntity = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomTypeId(roomTypeId);
        } catch (RoomTypeNotFoundException ex) {
           
        }
        newRoomEntity.setRoomTypeEntity(roomTypeEntity);
        em.persist(newRoomEntity);
        roomTypeEntity.getRoomEntities().add(newRoomEntity);
        em.flush();

        return newRoomEntity;
    }

    @Override
    public List<RoomEntity> retrieveAllRooms() {
        Query query = em.createQuery("SELECT r FROM RoomEntity r");

        return query.getResultList();
    }

    @Override
    public RoomEntity retrieveRoomByRoomTypeId(Long roomId) throws RoomNotFoundException {
        RoomEntity roomEntity = em.find(RoomEntity.class, roomId);

        if (roomEntity != null) {
            return roomEntity;
        } else {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }
    }

    @Override
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomNumber = :inRoomNumber");
        query.setParameter("inRoomNumber", roomNumber);

        try {
            return (RoomEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomNotFoundException("Room Number " + roomNumber + " does not exist!");
        }
    }

    @Override
    public RoomEntity retrieveRoomByRoomId(Long roomId) throws RoomNotFoundException {
        RoomEntity roomEntity = em.find(RoomEntity.class, roomId);

        if (roomEntity != null) {
            return roomEntity;
        } else {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }
    }

    @Override
    public List<RoomEntity> retrieveRoomsByReservationId(Long reservationId) {

        String qlString = "SELECT r FROM RoomEntity r "
                + "JOIN r.reservedRoomEntity rr "
                + "WHERE rr.reservationEntity.reservationId = :inReservationId";
        Query query = em.createQuery(qlString);
        query.setParameter("inReservationId", reservationId);

        List<RoomEntity> roomEntities = query.getResultList();
        return roomEntities;

    }

    @Override
    public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException {

        if (roomEntity.getRoomId() != null) {
            RoomEntity roomEntityToUpdate = retrieveRoomByRoomId(roomEntity.getRoomId());
            roomEntityToUpdate.setRoomNumber(roomEntity.getRoomNumber());
            roomEntityToUpdate.setStatus(roomEntity.getStatus());
            roomEntityToUpdate.setRoomTypeEntity(roomEntity.getRoomTypeEntity());

        } else {
            throw new RoomNotFoundException("Room ID not provided for room to be updated");
        }
    }

    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException, DeleteRoomException {
        RoomEntity roomEntityToRemove = retrieveRoomByRoomId(roomId);
        //if it is not used, it means that it is available
        if (roomEntityToRemove.getStatus().equals(roomStatusEnum.AVAILABLE)) {
            em.remove(roomEntityToRemove);
        } else {
            throw new DeleteRoomException("Room ID " + roomId + " is used and cannot be deleted!");
        }
    }

    @Override
    public void checkInGuest(Long roomId) throws RoomNotFoundException {

        RoomEntity roomEntity = retrieveRoomByRoomId(roomId);
        roomEntity.setStatus(roomStatusEnum.UNAVAILABLE);

    }

    @Override
    public void checkOutGuest(Long roomId) throws RoomNotFoundException {

        RoomEntity roomEntity = retrieveRoomByRoomId(roomId);
        roomEntity.setStatus(roomStatusEnum.AVAILABLE);

    }

}
