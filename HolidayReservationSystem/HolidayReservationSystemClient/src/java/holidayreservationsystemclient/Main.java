package holidayreservationsystemclient;

import java.text.ParseException;
import javax.xml.datatype.DatatypeConfigurationException;
import ws.client.partnerWebService.InvalidLoginCredentialException_Exception;
import ws.client.partnerWebService.RoomRateNotFoundException_Exception;


public class Main {

    
    public static void main(String[] args) throws ParseException, DatatypeConfigurationException, InvalidLoginCredentialException_Exception, RoomRateNotFoundException_Exception {
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }

}
