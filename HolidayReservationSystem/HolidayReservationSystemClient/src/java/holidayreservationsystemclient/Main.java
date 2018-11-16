package holidayreservationsystemclient;

import ejb.session.stateless.PartnerControllerRemote;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.xml.datatype.DatatypeConfigurationException;



public class Main {

    @EJB
    private static PartnerControllerRemote partnerControllerRemote;

    public static void main(String[] args) {
        try {
            MainApp mainApp = new MainApp(partnerControllerRemote);
            mainApp.runApp();
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
