package ejb.session.stateless;

import Records.PartnerRecord;
import Records.RoomTypeRecord;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import ws.client.partnerWebService.InvalidLoginCredentialException_Exception;
import ws.client.partnerWebService.PartnerEntity;
import ws.client.partnerWebService.PartnerWebService_Service;
import ws.client.partnerWebService.RoomRateNotFoundException_Exception;
import ws.client.partnerWebService.RoomTypeEntity;

@Stateless
@Local(PartnerControllerLocal.class)
@Remote(PartnerControllerRemote.class)
public class PartnerController implements PartnerControllerRemote, PartnerControllerLocal {

    @WebServiceRef(wsdlLocation = "https://localhost:8080/PartnerWebService/PartnerWebService?wsdl")
    private PartnerWebService_Service service;

    public PartnerController() {
    }

    @Override
    public PartnerRecord partnerLoginRemote(String username, String password) {
        System.err.println("partner login remote");
        PartnerEntity partnerEntity = new PartnerEntity();
        PartnerRecord partnerRecord = new PartnerRecord();

        try {
            System.err.println("try");
            partnerEntity = partnerLogin(username, password);

            partnerRecord.setUsername(partnerEntity.getUserName());
            partnerRecord.setPassword(partnerEntity.getPassword());

        } catch (InvalidLoginCredentialException_Exception ex) {

            System.err.println("login failede");
        }
        return partnerRecord;
    }

    private PartnerEntity partnerLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception {
        System.err.println("partner login wsdl");

        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.partnerLogin(username, password);
    }

    @Override
    public List<RoomTypeRecord> retrieveAvailRoomTypesRemote(String username, String password, Date checkInDate, Date checkOutDate) throws DatatypeConfigurationException {

        List<RoomTypeEntity> roomTypeEntities = new ArrayList<>();
        List<RoomTypeRecord> roomTypeRecords = new ArrayList<>();
        try {
            GregorianCalendar calendar1 = new GregorianCalendar();

            calendar1.setTime(checkInDate);
            XMLGregorianCalendar checkInDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar1);

            GregorianCalendar calendar2 = new GregorianCalendar();

            calendar2.setTime(checkOutDate);
            XMLGregorianCalendar checkOutDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar2);

            roomTypeEntities = retrieveAvailableRoomTypes(username, password, checkInDate2, checkOutDate2);
            for (RoomTypeEntity roomTypeEntity : roomTypeEntities) {
                RoomTypeRecord roomTypeRecord = new RoomTypeRecord();
                roomTypeRecord.setRoomTypeName(roomTypeEntity.getName());
                roomTypeRecords.add(roomTypeRecord);
            }

        } catch (InvalidLoginCredentialException_Exception ex) {

        }
        return roomTypeRecords;
    }

    private static java.util.List<ws.client.partnerWebService.RoomTypeEntity> retrieveAvailableRoomTypes(java.lang.String username, java.lang.String password, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAvailableRoomTypes(username, password, checkInDate, checkOutDate);
    }

    @Override
    public BigDecimal calculateAmtRemote(String username, String password, RoomTypeRecord roomTypeRecord, Date checkInDate, Date checkOutDate, Boolean online) throws DatatypeConfigurationException {
        BigDecimal amount = BigDecimal.ZERO;
        try {
            GregorianCalendar calendar1 = new GregorianCalendar();

            calendar1.setTime(checkInDate);
            XMLGregorianCalendar checkInDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar1);

            GregorianCalendar calendar2 = new GregorianCalendar();

            calendar2.setTime(checkOutDate);
            XMLGregorianCalendar checkOutDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar2);
            RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
            roomTypeEntity.setName(roomTypeRecord.getRoomTypeName());
            roomTypeEntity.setBed("");
            roomTypeEntity.setCapacity(0);
            roomTypeEntity.setDescription("");
            roomTypeEntity.setNumOfRooms(0);
            roomTypeEntity.setRanking(0);
            roomTypeEntity.setSize("");
            amount = calculateAmount(username, password, roomTypeEntity, checkInDate2, checkOutDate2, online);

        } catch (InvalidLoginCredentialException_Exception ex) {

        }
        return amount;
    }

    private static BigDecimal calculateAmount(java.lang.String username, java.lang.String password, ws.client.partnerWebService.RoomTypeEntity roomTypeEntity, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate, java.lang.Boolean online) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        try {
            return port.calculateAmount(username, password, roomTypeEntity, checkInDate, checkOutDate, online);
        } catch (RoomRateNotFoundException_Exception ex) {
            return BigDecimal.ZERO;
        }
    }

//    @Override
//    public Integer roomCountRemote(String username, String password, RoomTypeRecord roomTypeRecord, Date checkInDate, Date checkOutDate) throws DatatypeConfigurationException {
//        int count = 0;
//        try {
//            GregorianCalendar calendar1 = new GregorianCalendar();
//
//            calendar1.setTime(checkInDate);
//            XMLGregorianCalendar checkInDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar1);
//
//            GregorianCalendar calendar2 = new GregorianCalendar();
//
//            calendar2.setTime(checkOutDate);
//            XMLGregorianCalendar checkOutDate2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar2);
//
//            RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
//            roomTypeEntity.setName(roomTypeRecord.getRoomTypeName());
//            roomTypeEntity.setBed("");
//            roomTypeEntity.setCapacity(0);
//            roomTypeEntity.setDescription("");
//            roomTypeEntity.setNumOfRooms(0);
//            roomTypeEntity.setRanking(0);
//            roomTypeEntity.setSize("");
//            return retrieveAvailableRoomCount(username, password, roomTypeEntity, checkInDate2, checkOutDate2);
//        } catch (InvalidLoginCredentialException_Exception ex) {
//
//        }
//        return count;
//    }
    private static Integer retrieveAvailableRoomCount(java.lang.String username, java.lang.String password, ws.client.partnerWebService.RoomTypeEntity roomTypeEntity, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAvailableRoomCount(username, password, roomTypeEntity, checkInDate, checkOutDate);
    }

}
