/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kai_n
 */
@Stateless
@Local (PartnerSessionBeanLocal.class)
@Remote (PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

   
    @Override
    public PartnerEntity createNewPartner(PartnerEntity newPartnerEntity)
    {
        em.persist(newPartnerEntity);
        em.flush();
        
        return newPartnerEntity;
    }
    
    public List<PartnerEntity> retrieveAllPartners()
    {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        
        return query.getResultList();
    }


   
}
