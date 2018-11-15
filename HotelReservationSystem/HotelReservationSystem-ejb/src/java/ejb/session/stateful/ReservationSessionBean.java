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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
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

    @Resource
    private EJBContext eJBContext;

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
                if (j == 0) {
                    currNightDate = addDays(currNightDate, 0);
                } else {
                    currNightDate = addDays(currNightDate, 1);
                }
                ReservedNightEntity newReservedNightEntity = new ReservedNightEntity();
                newReservedNightEntity.setReservedRoomEntity(newReservedRoomEntity);
                //SETTING THE COST PER NIGHT
                rateTypeEnum currRateTypeEnum = rateTypeEnum.PUBLISHED;
                if (newReservationEntity.getOnlineReservation().equals(Boolean.FALSE)) {
                    currRateTypeEnum = rateTypeEnum.PUBLISHED;
                    RoomRateEntity roomRateEntity = roomRateSessionBeanLocal.retrieveRoomRateByRateType(roomTypeId, currRateTypeEnum);
                    roomRateId = roomRateEntity.getRoomRateId();

                } else {
                    //retrieve all the applicable roomrates linked to this roomtype
                    List<RoomRateEntity> roomRateEntities = new ArrayList<>();
                    roomRateEntities = roomTypeEntity.getRoomRateEntities();

                    roomRateId = findRoomRate(roomRateEntities, currNightDate);
                    System.out.println("Room Rate for Night: " + currNightDate + "has room rate: " + roomRateId);

                }

                RoomRateEntity roomRateEntity = roomRateSessionBeanLocal.retrieveRoomRateByRoomRateId(roomRateId);
                newReservedNightEntity.setRoomRateEntity(roomRateEntity);
                newReservedNightEntity.setAmount(roomRateEntity.getRatePerNight());
                System.out.println("Rate per Night: " + roomRateEntity.getRatePerNight());
                em.persist(newReservedNightEntity);

                newReservedNightEntities.add(newReservedNightEntity);
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
                System.out.println("Amount: " + reservationAmount);
            }
        }
        newReservationEntity.setReservationAmount(reservationAmount);

        GuestEntity guestEntity = new GuestEntity();
        try {
            guestEntity = guestSessionBeanLocal.retrieveGuestByGuestId(guestId);
        } catch (GuestNotFoundException ex) {
            eJBContext.setRollbackOnly();

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
            reservedRoomEntity.getReservationEntity();
            return reservedRoomEntity;
        } else {
            throw new ReservedRoomNotFoundException("ReservedRoom ID " + reservedRoomId + " does not exist!");
        }
    }

    @Override
    public Long linkReservedRoomToRoom(Long reservedRoomId, Long roomTypeId) {

        Long roomId = 0L;
        //if no room available
        ReservedRoomEntity currReservedRoomEntity = new ReservedRoomEntity();

        try {
            currReservedRoomEntity = retrieveReservedRoomByReservedRoomId(reservedRoomId);
        } catch (ReservedRoomNotFoundException ex) {

        }

        try {
            RoomTypeEntity currRoomTypeEntity = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomTypeId(roomTypeId);
            List<RoomEntity> roomEntities = currRoomTypeEntity.getRoomEntities();
            for (RoomEntity roomEntity : roomEntities) {
                if (roomEntity.getStatus() == roomStatusEnum.AVAILABLE) {

                    roomEntity.setStatus(roomStatusEnum.ALLOCATED);
//                    if (currReservedRoomEntity.getReservationEntity().getOnlineReservation() == Boolean.FALSE) {
//                        roomEntity.setStatus(roomStatusEnum.UNAVAILABLE);
//                    }
                    roomEntity.getReservedRoomEntities().add(currReservedRoomEntity);
                    //ERROR DUE TO THIS LINE
                    currReservedRoomEntity.setRoomEntity(roomEntity);
                    roomId = roomEntity.getRoomId();
                    break;
                } else if (roomEntity.getStatus() == roomStatusEnum.UNAVAILABLE) {

                    Date today = new Date();
                    Boolean checksOutToday = Boolean.FALSE;
                    //check if checkout date is equivalent to date
                    //need roomentity.reservedroom.reservation.checkoutdate
                    List<ReservationEntity> reservationEntities = roomSessionBeanLocal.retrieveReservationsByRoomId(roomEntity.getRoomId());

                    for (ReservationEntity reservationEntity : reservationEntities) {
                        if (isSameDay(today, reservationEntity.getCheckOutDate())) {
                            checksOutToday = Boolean.TRUE;
                            break;
                        }
                    }

                    if (checksOutToday) {
                        roomEntity.setStatus(roomStatusEnum.ALLOCATED);
//                        if (currReservedRoomEntity.getReservationEntity().getOnlineReservation() == Boolean.FALSE) {
//                            roomEntity.setStatus(roomStatusEnum.UNAVAILABLE);
//                        }
                        roomEntity.getReservedRoomEntities().add(currReservedRoomEntity);
                        currReservedRoomEntity.setRoomEntity(roomEntity);
                        roomId = roomEntity.getRoomId();
                        break;
                    }
                }
            }//ends going through each room
        } catch (RoomTypeNotFoundException ex) {

        }
        em.flush();
        //if returns 0L means it was not allocated
        return roomId;
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

    @Override
    public List<ReservationEntity> retrieveReservationsByGuestId(Long guestId) {

        String qlString = "SELECT r FROM ReservationEntity r WHERE r.guestEntity.guestId = :inGuestId";

        Query query = em.createQuery(qlString);
        query.setParameter("inGuestId", guestId);
        List<ReservationEntity> reservationEntities = new ArrayList<>();
        reservationEntities = query.getResultList();
        for (ReservationEntity reservationEntity : reservationEntities) {
            reservationEntity.getReservedRoomEntities();
        }
        return reservationEntities;
    }

    @Override
    public List<ReservedRoomEntity> retrieveReservedRoomsByReservationId(Long reservationId) {

        String qlString = "SELECT rr FROM ReservedRoomEntity rr WHERE rr.reservationEntity.reservationId = :inReservationId";

        Query query = em.createQuery(qlString);
        query.setParameter("inReservationId", reservationId);
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
            } else if (roomRateEntity.getRateType() == rateTypeEnum.PUBLISHED) {

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
            Boolean found = Boolean.FALSE;
            for (RoomRateEntity roomRateEntity : applicableRoomRates) {
                Boolean hasPeak = Boolean.FALSE;
                if (roomRateEntity.getRateType() == rateTypeEnum.PEAK) {
                    roomRateId = roomRateEntity.getRoomRateId();
                    found = Boolean.TRUE;
                    hasPeak = Boolean.TRUE;
                }
                if ((roomRateEntity.getRateType() == rateTypeEnum.PROMOTION) && (!hasPeak)) {
                    roomRateId = roomRateEntity.getRoomRateId();
                    found = Boolean.TRUE;
                }
            }
            if (!found) {

                for (RoomRateEntity roomRateEntity : applicableRoomRates) {
                    if (roomRateEntity.getRateType() == rateTypeEnum.NORMAL) {
                        roomRateId = roomRateEntity.getRoomRateId();
                    }
                }
            }
        }

        return roomRateId;
    }

    @Override
    public BigDecimal calculateAmount(RoomTypeEntity roomTypeEntity, Date checkInDate, Date checkOutDate, Boolean online) throws RoomRateNotFoundException {

        BigDecimal amount = BigDecimal.ZERO;

        RoomTypeEntity currRoomTypeEntity = new RoomTypeEntity();
        try {
            currRoomTypeEntity = roomTypeSessionBeanLocal.retrieveRoomTypeByRoomTypeId(roomTypeEntity.getRoomTypeId());
        } catch (RoomTypeNotFoundException ex) {

        }

        long diff = checkOutDate.getTime() - checkInDate.getTime();
        int numNights = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        Date currNightDate = checkInDate;
        Long roomRateId = 1L;
        //calculate for each night
        for (int i = 0; i < numNights; i++) {

            if (i == 0) {
                currNightDate = addDays(currNightDate, 0);
            } else {
                currNightDate = addDays(currNightDate, 1);
            }
            rateTypeEnum currRateTypeEnum = rateTypeEnum.PUBLISHED;
            System.out.println(online);
            if (!online) {
                //no change to the default
                System.out.println("This is a walk-in search room");
                RoomRateEntity roomRateEntity = roomRateSessionBeanLocal.retrieveRoomRateByRateType(currRoomTypeEntity.getRoomTypeId(), currRateTypeEnum);
                roomRateId = roomRateEntity.getRoomRateId();
            } else {
                System.out.println("This is an online search room");
                //online reservation-> compare the rates
                List<RoomRateEntity> roomRateEntities = new ArrayList<>();
                roomRateEntities = currRoomTypeEntity.getRoomRateEntities();

                roomRateId = findRoomRate(roomRateEntities, currNightDate);
                System.out.println("Room Rate for Night: " + currNightDate + "has room rate: " + roomRateId);

            }//ends online reservation rates comparison
            RoomRateEntity roomRateEntity = roomRateSessionBeanLocal.retrieveRoomRateByRoomRateId(roomRateId);
            System.out.println("Rate per Night: " + roomRateEntity.getRatePerNight());
            amount = amount.add(roomRateEntity.getRatePerNight());

        }

        return amount;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

}
