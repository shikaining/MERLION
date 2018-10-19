/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@Local (ReservationSessionBeanLocal.class)

public class ReservationSessionBean implements ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

   
    @Override
    public ReservationEntity createNewReservation (ReservationEntity newReservationEntity)
    {
        em.persist(newReservationEntity);
        em.flush();
        
        return newReservationEntity;
    }
    
    
    @Override
    public List<ReservationEntity> retrieveAllReservations()
    {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r");
        
        return query.getResultList();
    }
    
}
