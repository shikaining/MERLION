package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.PartnerEntity;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;

@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException {

        return partnerSessionBeanLocal.partnerLogin(username, password);

    }

}
