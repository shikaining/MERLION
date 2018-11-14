package ejb.session.stateless;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import ws.client.partnerWebService.InvalidLoginCredentialException_Exception;
import ws.client.partnerWebService.PartnerEntity;
import ws.client.partnerWebService.PartnerWebService_Service;

@Stateless
@Local(PartnerControllerLocal.class)
@Remote(PartnerControllerRemote.class)
public class PartnerController implements PartnerControllerRemote, PartnerControllerLocal {

    @WebServiceRef(wsdlLocation = "https://KAIKAI:8181/PartnerWebService/PartnerWebService?wsdl")
    private PartnerWebService_Service service;

    public PartnerController() {
    }

    public PartnerEntity partnerLoginRemote(String username, String password) {
        PartnerEntity partnerEntity = new PartnerEntity();
        try {
            return partnerLogin(username, password);
        } catch (InvalidLoginCredentialException_Exception ex) {

        }
        return partnerEntity;
    }

    private PartnerEntity partnerLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception {

        ws.client.partnerWebService.PartnerWebService port = service.getPartnerWebServicePort();
        return port.partnerLogin(username, password);
    }

}
