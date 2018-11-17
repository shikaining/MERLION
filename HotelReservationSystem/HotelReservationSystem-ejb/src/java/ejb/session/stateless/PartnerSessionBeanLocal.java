package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

public interface PartnerSessionBeanLocal {

    List<PartnerEntity> retrieveAllPartners();

    PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    PartnerEntity retrievePartnertByPartnerId(Long partnerId) throws PartnerNotFoundException;
}
