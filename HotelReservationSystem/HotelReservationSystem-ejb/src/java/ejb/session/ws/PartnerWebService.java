package ejb.session.ws;

import ejb.session.stateful.ReservationSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.PartnerEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import util.exception.InvalidLoginCredentialException;
import util.exception.RoomRateNotFoundException;

@WebService(serviceName = "PartnerWebService")
@Stateless
public class PartnerWebService {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password)
            throws InvalidLoginCredentialException {

        PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return partnerEntity;
    }

    @WebMethod(operationName = "retrieveAvailableRoomTypes")
    public List<RoomTypeEntity> retrieveAvailableRoomTypes(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate) throws InvalidLoginCredentialException {
        PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        List<RoomTypeEntity> availableRoomTypes = roomTypeSessionBeanLocal.retrieveAvailableRoomTypes(checkInDate, checkOutDate);
        System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return availableRoomTypes;
    }

    @WebMethod(operationName = "retrieveAvailableRoomCount")
    public Integer retrieveAvailableRoomCount(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "RoomTypeEntity") RoomTypeEntity roomTypeEntity,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate) throws InvalidLoginCredentialException {
        PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        Integer rooms = roomTypeSessionBeanLocal.retrieveAvailableRoomCount(roomTypeEntity, checkInDate, checkOutDate);
        System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return rooms;

    }

    @WebMethod(operationName = "calculateAmount")
    public BigDecimal calculateAmount(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password,
            @WebParam(name = "RoomTypeEntity") RoomTypeEntity roomTypeEntity,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate,
            @WebParam(name = "online") Boolean online) throws InvalidLoginCredentialException, RoomRateNotFoundException {

        PartnerEntity partnerEntity = partnerSessionBeanLocal.partnerLogin(username, password);
        BigDecimal amount = reservationSessionBeanLocal.calculateAmount(roomTypeEntity, checkInDate, checkOutDate, online);
        System.out.println("********** PartnerWebService.partnerLogin(): Partner " + partnerEntity.getUserName() + " login remotely via web service");
        return amount;

    }

}
