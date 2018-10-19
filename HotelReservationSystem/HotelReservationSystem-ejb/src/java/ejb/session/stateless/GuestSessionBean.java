/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.GuestEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.GuestNotFoundException;

/**
 *
 * @author kai_n
 */
@Stateless
@Local (GuestSessionBeanLocal.class)
@Remote (GuestSessionBeanRemote.class)
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    
    @Override
    public GuestEntity createNewCustomer(GuestEntity newGuestEntity)
    {
        em.persist(newGuestEntity);
        em.flush();
        
        return newGuestEntity;
    }
    
    
    @Override
    public List<GuestEntity> retrieveAllGuests()
    {
        Query query = em.createQuery("SELECT g FROM GuestEntity g");
        
        return query.getResultList();
    }
    
    
    @Override
    public GuestEntity retrieveGuestsByUsername (String username) throws GuestNotFoundException
    {
        Query query = em.createQuery("SELECT g FROM GuestEntity g WHERE g.userName = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (GuestEntity)query.getSingleResult();
        }
        catch(NoResultException|NonUniqueResultException ex)
        {
            throw new GuestNotFoundException("Guest with Username "+username+"does not exist!");
        }
    }

    
}
