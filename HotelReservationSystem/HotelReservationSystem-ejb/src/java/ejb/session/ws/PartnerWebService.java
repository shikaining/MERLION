package ejb.session.ws;

import ejb.session.stateful.ReservationSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.PartnerEntity;
import entity.ReservationEntity;
import entity.ReservedRoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

@WebService(serviceName = "PartnerWebService")
@Stateless
public class PartnerWebService {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");

    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password)
            throws InvalidLoginCredentialException {
        System.err.println("in server");

        PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        System.err.println("passed sessbean");
        em.detach(partnerEntity);
        partnerEntity.setReservationEntities(null);
        System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return partnerEntity;
    }

    @WebMethod(operationName = "retrieveAvailableRoomTypes")
    public List<RoomTypeEntity> retrieveAvailableRoomTypes(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate) throws InvalidLoginCredentialException {
        //PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        List<RoomTypeEntity> availableRoomTypes = roomTypeSessionBeanLocal.retrieveAvailableRoomTypes(checkInDate, checkOutDate);

        for (RoomTypeEntity roomTypeEntity : availableRoomTypes) {
            em.detach(roomTypeEntity);
            roomTypeEntity.setReservedRoomEntities(null);
            roomTypeEntity.setRoomEntities(null);
            roomTypeEntity.setRoomRateEntities(null);
        }

        //System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return availableRoomTypes;
    }

    @WebMethod(operationName = "retrieveAvailableRoomCount")
    public Integer retrieveAvailableRoomCount(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "RoomTypeEntity") RoomTypeEntity roomTypeEntity,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate) {
        //PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        Integer rooms = roomTypeSessionBeanLocal.retrieveAvailableRoomCount(roomTypeEntity, checkInDate, checkOutDate);
        //System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return rooms;
    }

    @WebMethod(operationName = "calculateAmount")
    public BigDecimal calculateAmount(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "RoomTypeId") Long roomTypeId,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate,
            @WebParam(name = "online") Boolean online) {

        BigDecimal amount = BigDecimal.ZERO;
        try {
            //PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
            amount = reservationSessionBeanLocal.calculateAmount(roomTypeId, checkInDate, checkOutDate, online);
            //System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
            return amount;
        } catch (RoomRateNotFoundException ex) {
            //Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return amount;
    }

    @WebMethod(operationName = "reserveForPartner")
    public Long reserve(@WebParam(name = "reservation") ReservationEntity reservationEntity,
            @WebParam(name = "partnerId") Long partnerId,
            @WebParam(name = "numOfRooms") int numOfRooms,
            @WebParam(name = "roomTypeId") Long roomTypeId) {

        Long reservationId = 0L;
        try {
            reservationId = reservationSessionBeanLocal.reserveForPartner(reservationEntity, partnerId, numOfRooms, roomTypeId);
        } catch (RoomTypeNotFoundException ex) {
            //Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RoomRateNotFoundException ex) {
            //Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return reservationId;
    }

    @WebMethod(operationName = "retrieveReservationByReservationId")
    public ReservationEntity retrieveReservationByReservationId(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "reservationId") Long reservationId) {

        ReservationEntity reservationEntity = new ReservationEntity();
        try {
            reservationEntity = reservationSessionBeanLocal.retrieveReservationByReservationId(reservationId);
            em.detach(reservationEntity);
            reservationEntity.setReservedRoomEntities(null);
            reservationEntity.setGuestEntity(null);
            reservationEntity.setPartnerEntity(null);
        } catch (ReservationNotFoundException ex) {
            //Logger.getLogger(PartnerWebService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return reservationEntity;
    }

    @WebMethod(operationName = "retrieveReservedRoomsyReservationId")
    public List<ReservedRoomEntity> retrieveReservedRoomsByReservationId(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "reservationId") Long reservationId) {

        List<ReservedRoomEntity> reservedRoomEntities = new ArrayList<>();

        reservedRoomEntities = reservationSessionBeanLocal.retrieveReservedRoomsByReservationId(reservationId);

        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
            em.detach(reservedRoomEntity);
            reservedRoomEntity.setCheckInDate(reservedRoomEntity.getReservationEntity().getCheckInDate());
            reservedRoomEntity.setCheckOutDate(reservedRoomEntity.getReservationEntity().getCheckOutDate());
            reservedRoomEntity.setRoomTypeName(reservedRoomEntity.getRoomTypeEntity().getName());
            reservedRoomEntity.setRoomTypeEntity(null);
            reservedRoomEntity.setReservationEntity(null);
            reservedRoomEntity.setReservedNightEntities(null);
            reservedRoomEntity.setRoomEntity(null);
        }

        return reservedRoomEntities;
    }

    @WebMethod(operationName = "retrieveReservedRoomsByPartnerId")
    public List<ReservedRoomEntity> retrieveReservedRoomsByPartnerId(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "partnerId") Long partnerId) {

        List<ReservedRoomEntity> reservedRoomEntities = new ArrayList<>();

        reservedRoomEntities = reservationSessionBeanLocal.retrieveReservedRoomByPartnerId(partnerId);

        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
            reservedRoomEntity.setCheckInDate(reservedRoomEntity.getReservationEntity().getCheckInDate());
            reservedRoomEntity.setCheckOutDate(reservedRoomEntity.getReservationEntity().getCheckOutDate());
            reservedRoomEntity.setRoomTypeName(reservedRoomEntity.getRoomTypeEntity().getName());

            em.detach(reservedRoomEntity);

            reservedRoomEntity.setRoomTypeEntity(null);
            reservedRoomEntity.setReservationEntity(null);
            reservedRoomEntity.setReservedNightEntities(null);
            reservedRoomEntity.setRoomEntity(null);
        }

        return reservedRoomEntities;
    }

}
