package ejb.session.stateless;

import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

@Stateless
@Local(RoomTypeSessionBeanLocal.class)
@Remote(RoomTypeSessionBeanRemote.class)
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity) {
        em.persist(newRoomTypeEntity);
        em.flush();

        return newRoomTypeEntity;
    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt");
        List<RoomTypeEntity> roomTypeEntities = new ArrayList<>();
        roomTypeEntities = query.getResultList();

        for (RoomTypeEntity roomTypeEntity : roomTypeEntities) {
            roomTypeEntity.getAmenities().size();
            roomTypeEntity.getRoomRateEntities().size();
            roomTypeEntity.getRoomEntities().size();
            roomTypeEntity.getReservedRoomEntities().size();
        }
        return roomTypeEntities;
    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = :inName");
        query.setParameter("inName", name);

        try {
            return (RoomTypeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type Name " + name + " does not exist!");
        }
    }

    @Override
    public void updateRoomType(RoomTypeEntity roomTypeEntity) throws RoomTypeNotFoundException {

        if (roomTypeEntity.getRoomTypeId() != null) {
            RoomTypeEntity roomTypeEntityToUpdate = retrieveRoomTypeByRoomTypeId(roomTypeEntity.getRoomTypeId());
            if (roomTypeEntityToUpdate.getName().equals(roomTypeEntity.getName())) {

                roomTypeEntityToUpdate.setDescription(roomTypeEntity.getDescription());
                roomTypeEntityToUpdate.setSize(roomTypeEntity.getSize());
                roomTypeEntityToUpdate.setBed(roomTypeEntity.getBed());
                roomTypeEntityToUpdate.setCapacity(roomTypeEntity.getCapacity());
                roomTypeEntityToUpdate.setAmenities(roomTypeEntity.getAmenities());

            }
        } else {
            throw new RoomTypeNotFoundException("Room Type ID not provided for room type to be updated");
        }
    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomTypeEntity = em.find(RoomTypeEntity.class, roomTypeId);

        if (roomTypeEntity != null) {
            return roomTypeEntity;
        } else {
            throw new RoomTypeNotFoundException("Room Type ID " + roomTypeId + " does not exist!");
        }
    }

    @Override
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException {
        RoomTypeEntity roomTypeEntityToRemove = retrieveRoomTypeByRoomTypeId(roomTypeId);
        if ((roomTypeEntityToRemove.getRoomEntities().isEmpty()) && (roomTypeEntityToRemove.getReservedRoomEntities().isEmpty()) && (roomTypeEntityToRemove.getRoomRateEntities().isEmpty())) {
            em.remove(roomTypeEntityToRemove);
        } else {
            throw new DeleteRoomTypeException("Room Type ID " + roomTypeId + " is being used and cannot be deleted!");
        }
    }

    @Override
    public List<RoomTypeEntity> retrieveAvailableRoomTypes(Date checkInDate, Date checkOutDate) {
        //retrieve all the room types
        List<RoomTypeEntity> allRoomTypes = retrieveAllRoomTypes();

        List<RoomTypeEntity> availableRoomTypes = new ArrayList<>();

        //for each room type,
        //get all the reservations under it
        for (RoomTypeEntity roomTypeEntity : allRoomTypes) {
            if (!roomTypeEntity.getRoomEntities().isEmpty()) {
                if (!roomTypeEntity.getReservedRoomEntities().isEmpty()) {
//                System.out.println(roomTypeEntity.getName());

                    String qlString = "SELECT rr FROM ReservedRoomEntity rr "
                            + "JOIN rr.roomTypeEntity rt "
                            + "WHERE rt.name = :inRoomType";

                    Query query = em.createQuery(qlString);
                    query.setParameter("inRoomType", roomTypeEntity.getName());
                    List<ReservedRoomEntity> reservedRooms = query.getResultList();

                    //loop through reservedrooms under this particular room type
                    //and check if there are any available rooms
                    int nonClashes = 0;
                    int clashes = 0;
                    for (ReservedRoomEntity reservedRoomEntity : reservedRooms) {
                        System.out.println("reservedRoom Id: " + reservedRoomEntity.getReservedRoomId());

                        ReservationEntity reservationEntity = new ReservationEntity();
                        reservationEntity = reservedRoomEntity.getReservationEntity();
                        Date reservedCheckInDate = reservationEntity.getCheckInDate();
                        Date reservedCheckOutDate = reservationEntity.getCheckOutDate();

                        if ((reservedCheckOutDate.compareTo(checkInDate) <= 0) && (reservedCheckInDate.compareTo(checkOutDate)) >= 0) {

                        } else {
                            clashes++;
                        }
                    }
                    if (clashes < roomTypeEntity.getReservedRoomEntities().size()) {
                        availableRoomTypes.add(roomTypeEntity);
                    }
                } else {
                    availableRoomTypes.add(roomTypeEntity);
                }
            }
        }

        //return statement 
        return allRoomTypes;
    }

    @Override
    public Integer retrieveAvailableRoomCount(RoomTypeEntity roomTypeEntity, Date checkInDate, Date checkOutDate) {

        int clashes = 0;
        int nonClashes = 0;
        if (!roomTypeEntity.getReservedRoomEntities().isEmpty()) {
            //get all the reserved rooms
            String qlString = "SELECT rr FROM ReservedRoomEntity rr "
                    + "JOIN rr.roomTypeEntity rt "
                    + "WHERE rt.name = :inRoomType";

            Query query = em.createQuery(qlString);
            query.setParameter("inRoomType", roomTypeEntity.getName());
            List<ReservedRoomEntity> reservedRooms = query.getResultList();

            for (ReservedRoomEntity reservedRoomEntity : reservedRooms) {

                ReservationEntity reservationEntity = new ReservationEntity();
                reservationEntity = reservedRoomEntity.getReservationEntity();
                Date reservedCheckInDate = reservationEntity.getCheckInDate();
                Date reservedCheckOutDate = reservationEntity.getCheckOutDate();

                if ((reservedCheckOutDate.compareTo(checkInDate) <= 0) && (reservedCheckInDate.compareTo(checkOutDate)) >= 0) {
                    
                } else {
                    clashes++;
                }
            }
            nonClashes = (roomTypeEntity.getRoomEntities().size()) - clashes;
        } else {

            nonClashes = roomTypeEntity.getRoomEntities().size();
        }
        return nonClashes;
    }
}
