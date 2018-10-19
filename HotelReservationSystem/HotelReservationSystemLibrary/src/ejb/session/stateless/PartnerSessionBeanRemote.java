package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;


public interface PartnerSessionBeanRemote {
     
    PartnerEntity createNewPartner(PartnerEntity newPartnerEntity);

    List<PartnerEntity> retrieveAllPartners();
    
}
