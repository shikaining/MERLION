package ejb.session.stateful;

import ejb.session.stateless.GuestSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.rateTypeEnum;
import util.enumeration.roomStatusEnum;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

@Stateful
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private GuestSessionBeanLocal guestSessionBeanLocal;
    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;
    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;
    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public ReservationEntity reserve(ReservationEntity newReservationEntity, Long guestId, int numOfRooms, Long roomTypeId) throws RoomTypeNotFoundException, RoomRateNotFoundException {

        RoomTypeEntity roomTypeEntity = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomTypeId(roomTypeId);
        //CREATE RESERVATION
        em.persist(newReservationEntity);

        //CREATE RESERVED ROOMS
        List<ReservedRoomEntity> newReservedRoomEntities = new ArrayList<>();
        for (int i = 0; i < numOfRooms; i++) {
            ReservedRoomEntity newReservedRoomEntity = new ReservedRoomEntity();
            newReservedRoomEntity.setRoomTypeEntity(roomTypeEntity);
            roomTypeEntity.getReservedRoomEntities().add(newReservedRoomEntity);
            newReservedRoomEntity.setReservationEntity(newReservationEntity);
            em.persist(newReservedRoomEntity);

            long diff = newReservationEntity.getCheckOutDate().getTime() - newReservationEntity.getCheckInDate().getTime();
            int numNights = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            //CREATE RESERVED NIGHTS
            List<ReservedNightEntity> newReservedNightEntities = new ArrayList<>();
            Date currNightDate = newReservationEntity.getCheckInDate();
            Long roomRateId = 1L;
            for (int j = 0; j < numNights; j++) {
                currNightDate = addDays(currNightDate, j);
                ReservedNightEntity newReservedNightEntity = new ReservedNightEntity();
                newReservedNightEntity.setReservedRoomEntity(newReservedRoomEntity);
                //SETTING THE COST PER NIGHT
                rateTypeEnum currRateTypeEnum = rateTypeEnum.PUBLISHED;
                if (newReservationEntity.getOnlineReservation().equals(Boolean.FALSE)) {
                    currRateTypeEnum = rateTypeEnum.PUBLISHED;

                } else {
                    //retrieve all the applicable roomrates linked to this roomtype
                    List<RoomRateEntity> roomRateEntities = new ArrayList<>();
                    roomRateEntities = roomTypeEntity.getRoomRateEntities();

                    if (roomRateEntities.size() == 1) {
                        currRateTypeEnum = rateTypeEnum.NORMAL;
                        roomRateId = roomRateEntities.get(0).getRoomRateId();
                    } else {

                        roomRateId = findRoomRate(roomRateEntities, currNightDate);

                    }//ends else

                    RoomRateEntity roomRateEntity = roomRateSessionBeanLocal.retrieveRoomRateByRoomRateId(roomRateId);
                    newReservedNightEntity.setRoomRateEntity(roomRateEntity);
                    newReservedNightEntity.setAmount(roomRateEntity.getRatePerNight());
                    em.persist(newReservedNightEntity);

                    newReservedNightEntities.add(newReservedNightEntity);
                }
            }//ends for loop

            newReservedRoomEntity.setReservedNightEntities(newReservedNightEntities);
            //SETTLED RESERVED ROOM

            newReservedRoomEntities.add(newReservedRoomEntity);
        }//ends creating each reserved room
        newReservationEntity.setReservedRoomEntities(newReservedRoomEntities);

        BigDecimal reservationAmount = new BigDecimal(BigInteger.ZERO);
        for (ReservedRoomEntity reservedRoomEntity : newReservedRoomEntities) {
            List<ReservedNightEntity> reservedNightEntities = reservedRoomEntity.getReservedNightEntities();
            for (ReservedNightEntity reservedNightEntity : reservedNightEntities) {
                reservationAmount = reservationAmount.add(reservedNightEntity.getAmount());
            }
        }
        newReservationEntity.setReservationAmount(reservationAmount);

        GuestEntity guestEntity = new GuestEntity();
        try {
            guestEntity = guestSessionBeanLocal.retrieveGuestByGuestId(guestId);
        } catch (GuestNotFoundException ex) {

        }
        newReservationEntity.setGuestEntity(guestEntity);
        //SETTLED RESERVATION
        em.flush();
        em.refresh(newReservationEntity);
        return newReservationEntity;
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

        ReservationEntity reservationEntity = em.find(ReservationEntity.class,
                reservationId);

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
        ReservedRoomEntity reservedRoomEntity = em.find(ReservedRoomEntity.class,
                reservedRoomId);

        if (reservedRoomEntity != null) {
            return reservedRoomEntity;
        } else {
            throw new ReservedRoomNotFoundException("ReservedRoom ID " + reservedRoomId + " does not exist!");
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

                    roomEntity.setStatus(roomStatusEnum.ALLOCATED);
                    roomEntity.getReservedRoomEntities().add(reservedRoomEntity);
                    reservedRoomEntity.setRoomEntity(roomEntity);
                    allocated = true;
                    break;
                } else if (roomEntity.getStatus() == roomStatusEnum.UNAVAILABLE) {

                    Date date = new Date();

                    roomEntity.setStatus(roomStatusEnum.ALLOCATED);
                    roomEntity.getReservedRoomEntities().add(reservedRoomEntity);
                    reservedRoomEntity.setRoomEntity(roomEntity);
                    allocated = true;
                    break;

                }
            }
        } catch (RoomTypeNotFoundException ex) {

        }

//        if (allocated == Boolean.FALSE) {
//            //not possible but put here first
//        }
    }//ends method 

    @Override
    public List<ReservedRoomEntity> retrieveReservedRoomByGuestId(Long guestId) {

        String qlString = "SELECT rr FROM ReservedRoomEntity rr "
                + "JOIN rr.reservationEntity r "
                + "WHERE r.guestEntity.guestId = :inGuestId";

        Query query = em.createQuery(qlString);
        query.setParameter("inGuestId", guestId);
        List<ReservedRoomEntity> reservedRoomEntities = new ArrayList<>();
        reservedRoomEntities = query.getResultList();
        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
            reservedRoomEntity.getRoomEntity();
            reservedRoomEntity.getReservationEntity();
        }
        return reservedRoomEntities;
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    @Override
    public Long findRoomRate(List<RoomRateEntity> roomRateEntities, Date currNightDate) {
        Long roomRateId = 1L;
        List<RoomRateEntity> applicableRoomRates = new ArrayList<>();
        //roomRateId = findRoomRate(roomRateEntities, currNightDate, roomRateId);
        for (RoomRateEntity roomRateEntity : roomRateEntities) {

            if (roomRateEntity.getValidityStart() != null) {
                //check if applicable
                Boolean applicable = Boolean.FALSE;
                if ((currNightDate.compareTo(roomRateEntity.getValidityStart()) >= 0) && (currNightDate.compareTo(roomRateEntity.getValidityEnd()) <= 0)) {
                    applicable = Boolean.TRUE;
                    applicableRoomRates.add(roomRateEntity);
                }
            } else {
                //normal rate
                applicableRoomRates.add(roomRateEntity);
            }
        }
        //now compare the priority of the rates
        if (applicableRoomRates.size() == 3) {
            //means is normal, promo & peak
            for (RoomRateEntity roomRateEntity : applicableRoomRates) {
                if (roomRateEntity.getRateType() == rateTypeEnum.PROMOTION) {
                    roomRateId = roomRateEntity.getRoomRateId();
                }
            }
        } else {
            for (RoomRateEntity roomRateEntity : applicableRoomRates) {
                Boolean hasPeak = Boolean.FALSE;
                if (roomRateEntity.getRateType() == rateTypeEnum.PEAK) {
                    roomRateId = roomRateEntity.getRoomRateId();
                    hasPeak = Boolean.TRUE;
                }
                if ((roomRateEntity.getRateType() == rateTypeEnum.PROMOTION) && (!hasPeak)) {
                    roomRateId = roomRateEntity.getRoomRateId();
                }
            }
        }

        return roomRateId;
    }
}
