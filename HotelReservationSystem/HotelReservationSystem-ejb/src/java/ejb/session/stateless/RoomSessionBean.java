package ejb.session.stateless;

import ejb.session.stateful.ReservationSessionBeanLocal;
import entity.ReportLineItemEntity;
import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.Date;
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
import util.enumeration.exceptionTypeEnum;
import util.enumeration.roomStatusEnum;
import util.exception.DeleteRoomException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

@Stateless
@Local(RoomSessionBeanLocal.class)
@Remote(RoomSessionBeanRemote.class)
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

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
    public RoomEntity retrieveRoomsByRoomId(Long roomId) throws RoomNotFoundException {
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
            roomEntity.getRoomTypeEntity();
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
    public List<ReservationEntity> retrieveReservationsByRoomId(Long roomId) {

        String qlString = "SELECT rv FROM ReservationEntity rv "
                + "JOIN rv.reservedRoomEntity rr "
                + "WHERE rr.roomEntity.roomId = :inRoomId";
        Query query = em.createQuery(qlString);
        query.setParameter("inRoomId", roomId);

        List<ReservationEntity> reservationEntities = query.getResultList();
        return reservationEntities;

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
        RoomTypeEntity roomTypeEntity = roomEntityToRemove.getRoomTypeEntity();
        //if it is not used, it means that it is available
        if (roomEntityToRemove.getStatus().equals(roomStatusEnum.AVAILABLE)) {
            roomTypeEntity.getRoomEntities().remove(roomEntityToRemove);
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

    @Override
    public void doAllocateRooms() {

        List<RoomTypeEntity> roomTypeEntities = roomTypeSessionBeanLocal.retrieveAllRoomTypes();

        //FOR EACH ROOM TYPE, RETRIEVE ALL THE RESERVED ROOM ENTITIES
        for (RoomTypeEntity roomTypeEntity : roomTypeEntities) {
            List<ReservedRoomEntity> reservedRoomEntities = roomTypeEntity.getReservedRoomEntities();
            System.out.println("The reserved room entities to be allocated are: " + reservedRoomEntities.toString());
            //FOR EACH RESERVEDROOM OF THE CURRENT ROOM TYPE

            if (!reservedRoomEntities.isEmpty()) {
                for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
                    Long roomId = reservationSessionBeanLocal.linkReservedRoomToRoom(reservedRoomEntity.getReservedRoomId(), roomTypeEntity.getRoomTypeId());
                    System.out.println("RoomId: " + roomId);
//                    //IF NO ROOMS FOR CURRENT ROOM TYPE
                    if (roomId == 0L) {
                        ReportLineItemEntity reportLineItemEntity = new ReportLineItemEntity();

                        Boolean reallocated = Boolean.FALSE;
                        while (!reallocated) //IF NO ROOMS IN HOTEL ANYMORE
                        {
                            System.out.println("Room Type: " + roomTypeEntity.getName());
                            if (roomTypeEntity.getName().equals("Grand Suite")) {
                                //must throw second type of exception
                                Date allocationDate = new Date();
                                reportLineItemEntity.setAllocationDate(allocationDate);
                                reportLineItemEntity.setOriginalRoomId(reservedRoomEntity.getReservedRoomId());
                                reportLineItemEntity.setOriginalRoomTypeId(roomTypeEntity.getRoomTypeId());
                                reportLineItemEntity.setTypeEnum(exceptionTypeEnum.EXCEPTIONTWO);
                                reportLineItemEntity.setMessageToAdmin("Message");
                                reportLineItemEntity.setMessageToGuest("Message");
                                em.persist(reportLineItemEntity);
                                reallocated = Boolean.TRUE;
                            } else {

                                //IF NO ROOMS FOR CURRENT ROOM TYPE BUT CAN TRY THE NEXT ROOM TYPE
                                //REMOVE RESERVED ROOM FROM CURRENT ROOM TYPE
//                                roomTypeEntity.getReservedRoomEntities().remove(reservedRoomEntity);
                                //CHANGE ROOM TYPE OF RR TO NEXT ROOM TYPE
                                RoomTypeEntity nextRoomTypeEntity = new RoomTypeEntity();
                                Long nextRoomTypeEntityId = roomTypeEntity.getRoomTypeId() + 1L;
                                System.out.println("Next Room Type Id: " + nextRoomTypeEntity + " has room type: " + roomTypeEntity.getName());
                                try {
                                    nextRoomTypeEntity = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomTypeId(nextRoomTypeEntityId);
                                    System.out.println("Next Room Type Entity: " + nextRoomTypeEntity.getName());
                                } catch (RoomTypeNotFoundException ex) {

                                }
                                reservedRoomEntity.setRoomTypeEntity(nextRoomTypeEntity);
                                System.out.println("Set new room type entity of reserved room. ");
                                //ADD RESERVED ROOM TO THE NEXT ROOM TYPE
                                nextRoomTypeEntity.getReservedRoomEntities().add(reservedRoomEntity);
                                System.out.println("Added reserved room entity. ");
                                em.flush();
                                System.out.println("Flushed?? ");

                                Date allocationDate = new Date();
                                reportLineItemEntity.setAllocationDate(allocationDate);
                                reportLineItemEntity.setOriginalRoomId(reservedRoomEntity.getReservedRoomId());
                                reportLineItemEntity.setOriginalRoomTypeId(roomTypeEntity.getRoomTypeId());
                                reportLineItemEntity.setNewRoomTypeId(nextRoomTypeEntityId);
                                reportLineItemEntity.setTypeEnum(exceptionTypeEnum.EXCEPTIONONE);
                                reportLineItemEntity.setMessageToAdmin("Message");
                                reportLineItemEntity.setMessageToGuest("Message");
                                em.persist(reportLineItemEntity);
                                reallocated = Boolean.TRUE;
                            }
                        }//ends while not allocated
                        em.flush();
                    }//ends if roomId == 0L
                    else {
                        //valid room id was returned so
                        //successful allocation!
                    }
                }//ends each reserved room
            }//ENDS IF got reserved rooms under this room type
        }//ends each room type
    }//ends allocation method

    @Override
    public ReportLineItemEntity retrieveLastReportLineItem() {

        Query query = em.createQuery("SELECT MAX(re.reportLineItemId) FROM ReportLineItemEntity re");
        return (ReportLineItemEntity) query.getSingleResult();
    }

}//ends class
