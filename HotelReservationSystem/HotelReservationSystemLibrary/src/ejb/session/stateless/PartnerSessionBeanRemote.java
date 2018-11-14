package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import util.exception.InvalidLoginCredentialException;

public interface PartnerSessionBeanRemote {

    PartnerEntity createNewPartner(PartnerEntity newPartnerEntity);

    List<PartnerEntity> retrieveAllPartners();

    PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

}
