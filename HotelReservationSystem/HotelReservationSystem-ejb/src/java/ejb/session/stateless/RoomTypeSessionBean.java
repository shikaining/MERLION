package ejb.session.stateless;

import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
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
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

@Stateless
@Local(RoomTypeSessionBeanLocal.class)
@Remote(RoomTypeSessionBeanRemote.class)
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @Override
    public RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity) {

        em.persist(newRoomTypeEntity);
        em.flush();

        return newRoomTypeEntity;
    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomTypes() {

        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt");
        List<RoomTypeEntity> roomTypeEntities = query.getResultList();

        for (RoomTypeEntity roomTypeEntity : roomTypeEntities) {
            roomTypeEntity.getReservedRoomEntities().size();
        }

        return roomTypeEntities;
    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = :inName");
        query.setParameter("inName", name);

        try {
            RoomTypeEntity roomTypeEntity = (RoomTypeEntity) query.getSingleResult();
            roomTypeEntity.getRoomEntities().size();
            roomTypeEntity.getReservedRoomEntities().size();
            roomTypeEntity.getRoomRateEntities().size();
            return roomTypeEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type Name " + name + " does not exist!");
        }
    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByRank(int rank) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.ranking = :inRank");
        query.setParameter("inRank", rank);
        //System.out.println(rank);

        try {
            RoomTypeEntity roomTypeEntity = (RoomTypeEntity) query.getSingleResult();
            roomTypeEntity.getRoomEntities().size();
            roomTypeEntity.getReservedRoomEntities().size();
            roomTypeEntity.getRoomRateEntities().size();
            //System.out.println(roomTypeEntity.getRoomTypeId());
            return roomTypeEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type Rank " + rank + " does not exist!");
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
                roomTypeEntityToUpdate.setRanking(roomTypeEntity.getRanking());

            }
        } else {
            throw new RoomTypeNotFoundException("Room Type ID not provided for room type to be updated");
        }
    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomTypeEntity = em.find(RoomTypeEntity.class, roomTypeId);

        if (roomTypeEntity != null) {
            roomTypeEntity.getRoomEntities().size();
            roomTypeEntity.getRoomRateEntities().size();
            roomTypeEntity.getReservedRoomEntities().size();
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

        List<RoomTypeEntity> allRoomTypes = retrieveAllRoomTypes();
        List<RoomTypeEntity> availableRoomTypes = new ArrayList<>();

        for (RoomTypeEntity roomTypeEntity : allRoomTypes) {

            if (!roomTypeEntity.getRoomEntities().isEmpty()) {
                //System.out.println(roomTypeEntity.getName() + " has rooms. ");

                if (!roomTypeEntity.getReservedRoomEntities().isEmpty()) {
                    //System.out.println(roomTypeEntity.getName() + " has reservations. ");

                    List<ReservedRoomEntity> reservedRooms = new ArrayList<>();
                    reservedRooms = retrieveReservedRoomsByRoomTypeName(roomTypeEntity.getName());

                    int nonClashes = 0;
                    int clashes = 0;
                    for (ReservedRoomEntity reservedRoomEntity : reservedRooms) {

                        //System.out.println("reservedRoom Id: " + reservedRoomEntity.getReservedRoomId());
                        ReservationEntity reservationEntity = reservedRoomEntity.getReservationEntity();
                        Date reservedCheckInDate = reservationEntity.getCheckInDate();
                        Date reservedCheckOutDate = reservationEntity.getCheckOutDate();

                        if ((reservedCheckOutDate.compareTo(checkInDate) <= 0) || (reservedCheckInDate.compareTo(checkOutDate)) >= 0) {

                        } else {
                            clashes++;
                        }
                    }

                    if (clashes < roomTypeEntity.getRoomEntities().size()) {
//                        System.out.println("Clashes: " + clashes);
//                        System.out.println("Room Entities size: " + roomTypeEntity.getRoomEntities().size());
                        availableRoomTypes.add(roomTypeEntity);
                    }
                } else {
                    //System.out.println("This room type has no reservedrooms. ");
                    availableRoomTypes.add(roomTypeEntity);
                }
                // System.out.println("Available room types: " + availableRoomTypes.toString());
            } else {
                // System.out.println("This room type has no rooms. ");
            }
        }

        //return statement 
        return availableRoomTypes;
    }

    @Override
    public Integer retrieveAvailableRoomCount(RoomTypeEntity roomTypeEntity, Date checkInDate, Date checkOutDate) {

        int clashes = 0;
        int nonClashes = 0;
        RoomTypeEntity currRoomTypeEntity = new RoomTypeEntity();

        try {
            currRoomTypeEntity = retrieveRoomTypeByRoomTypeId(roomTypeEntity.getRoomTypeId());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
        }

        if (!currRoomTypeEntity.getReservedRoomEntities().isEmpty()) {

            List<ReservedRoomEntity> reservedRooms = new ArrayList<>();
            reservedRooms = retrieveReservedRoomsByRoomTypeName(currRoomTypeEntity.getName());

            //System.out.println(currRoomTypeEntity.getName() + "has " + reservedRooms.size() + "rooms. ");
            for (ReservedRoomEntity reservedRoomEntity : reservedRooms) {

                ReservationEntity reservationEntity = reservedRoomEntity.getReservationEntity();
                Date reservedCheckInDate = reservationEntity.getCheckInDate();
                Date reservedCheckOutDate = reservationEntity.getCheckOutDate();

                if ((reservedCheckOutDate.compareTo(checkInDate) <= 0) || (reservedCheckInDate.compareTo(checkOutDate)) >= 0) {
                    //System.out.println("No Clash Detected for reservedRoomId: " + reservedRoomEntity.getReservedRoomId());
                } else {
                    clashes++;
                    //System.out.println("Clash Detected for reservedRoomId: " + reservedRoomEntity.getReservedRoomId());
                }
            }

            nonClashes = (roomTypeEntity.getRoomEntities().size()) - clashes;

        } else {

            nonClashes = roomTypeEntity.getRoomEntities().size();
        }
        //System.out.println("Non-clashes " + nonClashes);
        return nonClashes;
    }

    @Override
    public List<ReservedRoomEntity> retrieveReservedRoomsByRoomTypeName(String roomTypeName) {

        String qlString = "SELECT rr FROM ReservedRoomEntity rr "
                + "JOIN rr.roomTypeEntity rt "
                + "WHERE rt.name = :inRoomType";

        Query query = em.createQuery(qlString);
        query.setParameter("inRoomType", roomTypeName);
        List<ReservedRoomEntity> reservedRooms = new ArrayList<>();
        reservedRooms = query.getResultList();
        for (ReservedRoomEntity reservedRoomEntity : reservedRooms) {
            reservedRoomEntity.getReservationEntity();
        }
        return reservedRooms;
    }
}
