package datamodel.ws;

import java.math.BigDecimal;
import javax.xml.ws.WebServiceRef;
import ws.client.partnerWebService.InvalidLoginCredentialException_Exception;
import ws.client.partnerWebService.PartnerWebService_Service;
import ws.client.partnerWebService.RoomRateNotFoundException_Exception;

public class SearchRoomController {

    @WebServiceRef(wsdlLocation = "https://KAIKAI:8080/PartnerWebService/PartnerWebService?wsdl")
    private PartnerWebService_Service service;

    public SearchRoomController() {
    }

    private static java.util.List<ws.client.partnerWebService.RoomTypeEntity> retrieveAvailableRoomTypes(java.lang.String username, java.lang.String password, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAvailableRoomTypes(username, password, checkInDate, checkOutDate);
    }

    private static BigDecimal calculateAmount(java.lang.String username, java.lang.String password, ws.client.partnerWebService.RoomTypeEntity roomTypeEntity, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate, java.lang.Boolean online) throws RoomRateNotFoundException_Exception, InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.calculateAmount(username, password, roomTypeEntity, checkInDate, checkOutDate, online);
    }

    private static Integer retrieveAvailableRoomCount(java.lang.String username, java.lang.String password, ws.client.partnerWebService.RoomTypeEntity roomTypeEntity, javax.xml.datatype.XMLGregorianCalendar checkInDate, javax.xml.datatype.XMLGregorianCalendar checkOutDate) throws InvalidLoginCredentialException_Exception {
        ws.client.partnerWebService.PartnerWebService_Service service = new ws.client.partnerWebService.PartnerWebService_Service();
        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAvailableRoomCount(username, password, roomTypeEntity, checkInDate, checkOutDate);
    }

}
