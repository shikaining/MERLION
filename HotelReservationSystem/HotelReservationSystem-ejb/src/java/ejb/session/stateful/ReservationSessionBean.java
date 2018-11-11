package ejb.session.stateful;

import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.roomStatusEnum;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

@Stateful
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public ReservationEntity createNewReservation(ReservationEntity newReservationEntity) {
        em.persist(newReservationEntity);
        em.flush();

        return newReservationEntity;
    }

    @Override
    public ReservedRoomEntity createNewReservedRoom(ReservedRoomEntity newReservedRoomEntity) {
        em.persist(newReservedRoomEntity);
        em.flush();

        return newReservedRoomEntity;
    }

    @Override
    public ReservedNightEntity createNewReservedNight(ReservedNightEntity newReservedNightEntity) {
        em.persist(newReservedNightEntity);
        em.flush();

        return newReservedNightEntity;
    }

    @Override
    public List<ReservationEntity> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r");
        List<ReservationEntity> reservationEntities = query.getResultList();
        for (ReservationEntity reservationEntity : reservationEntities) {
            reservationEntity.getReservedRoomEntities().size();
            reservationEntity.getGuestEntity();
        }
        return reservationEntities;
    }

    @Override
    public List<ReservedRoomEntity> retrieveAllReservedRooms() {
        Query query = em.createQuery("SELECT rr FROM ReservedRoomEntity rr");
        List<ReservedRoomEntity> reservedRoomEntities = query.getResultList();
        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
            reservedRoomEntity.getReservedNightEntities().size();
            reservedRoomEntity.getReservationEntity();
            reservedRoomEntity.getRoomEntity();
            reservedRoomEntity.getRoomTypeEntity();

        }
        return reservedRoomEntities;

    }

    @Override
    public ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
        ReservationEntity reservationEntity = em.find(ReservationEntity.class, reservationId);

        if (reservationEntity != null) {
            reservationEntity.getReservedRoomEntities().size();
            reservationEntity.getGuestEntity();
            return reservationEntity;
        } else {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }
    }

    @Override
    public ReservedRoomEntity retrieveReservedRoomByReservedRoomId(Long reservedRoomId) throws ReservedRoomNotFoundException {
        ReservedRoomEntity reservedRoomEntity = em.find(ReservedRoomEntity.class, reservedRoomId);

        if (reservedRoomEntity != null) {
            return reservedRoomEntity;
        } else {
            throw new ReservedRoomNotFoundException("ReservedRoom ID " + reservedRoomId + " does not exist!");
        }
    }

    @Override
    public void updateReservation(ReservationEntity reservationEntity) throws ReservationNotFoundException {

        if (reservationEntity.getReservationId() != null) {
            ReservationEntity reservationEntityToUpdate = retrieveReservationByReservationId(reservationEntity.getReservationId());
            reservationEntityToUpdate.setGuestEntity(reservationEntity.getGuestEntity());
            reservationEntityToUpdate.setReservationAmount(reservationEntity.getReservationAmount());
            reservationEntityToUpdate.setReservedRoomEntities(reservationEntity.getReservedRoomEntities());

            em.flush();

        } else {
            throw new ReservationNotFoundException("Reservation ID not provided for reservation to be updated");
        }
    }

    @Override
    public void updateReservedRoom(ReservedRoomEntity reservedRoomEntity) throws ReservedRoomNotFoundException {

        if (reservedRoomEntity.getReservedRoomId() != null) {
            ReservedRoomEntity reservedRoomEntityToUpdate = retrieveReservedRoomByReservedRoomId(reservedRoomEntity.getReservedRoomId());
            reservedRoomEntityToUpdate.setReservedNightEntities(reservedRoomEntity.getReservedNightEntities());
            em.flush();
        } else {
            throw new ReservedRoomNotFoundException("ReservedRoom ID not provided for reserved room to be updated");
        }
    }

    @Override
    public void linkReservedRoomToRoom(Long reservedRoomId, Long roomTypeId) {

        ReservedRoomEntity reservedRoomEntity = new ReservedRoomEntity();

        //retrieve the rooms that are either available or are unavailable but checking out today
        try {
            reservedRoomEntity = retrieveReservedRoomByReservedRoomId(reservedRoomId);
        } catch (ReservedRoomNotFoundException ex) {

        }

        /*
        retrieve the rooms under this room type that are either available or unavailable but checkOutDate = today
         */
        Boolean allocated = Boolean.FALSE;
        try {
            RoomTypeEntity roomTypeEntity = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomTypeId(roomTypeId);
            List<RoomEntity> roomEntities = roomTypeEntity.getRoomEntities();
            for (RoomEntity roomEntity : roomEntities) {
                if (roomEntity.getStatus() == roomStatusEnum.AVAILABLE) {
                    roomEntity.setCheckInDate(reservedRoomEntity.getReservationEntity().getCheckInDate());
                    roomEntity.setCheckOutDate(reservedRoomEntity.getReservationEntity().getCheckOutDate());
                    roomEntity.setStatus(roomStatusEnum.ALLOCATED);
                    roomEntity.setReservedRoomEntity(reservedRoomEntity);
                    reservedRoomEntity.setRoomEntity(roomEntity);
                    allocated = true;
                    break;
                } else if (roomEntity.getStatus() == roomStatusEnum.UNAVAILABLE) {

                    Date date = new Date();

                    if (roomEntity.getCheckOutDate().compareTo(date) == 0) {
                        roomEntity.setCheckInDate(reservedRoomEntity.getReservationEntity().getCheckInDate());
                        roomEntity.setCheckOutDate(reservedRoomEntity.getReservationEntity().getCheckOutDate());
                        roomEntity.setStatus(roomStatusEnum.ALLOCATED);
                        roomEntity.setReservedRoomEntity(reservedRoomEntity);
                        reservedRoomEntity.setRoomEntity(roomEntity);
                        allocated = true;
                        break;
                    }
                }
            }
        } catch (RoomTypeNotFoundException ex) {

        }

//        if (allocated == Boolean.FALSE) {
//            //not possible but put here first
//        }

    }//ends method 

}
