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
@Local(GuestSessionBeanLocal.class)
@Remote(GuestSessionBeanRemote.class)
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public GuestEntity createNewGuest(GuestEntity newGuestEntity) {
        em.persist(newGuestEntity);
        em.flush();

        return newGuestEntity;
    }

    @Override
    public List<GuestEntity> retrieveAllGuests() {

        Query query = em.createQuery("SELECT g FROM GuestEntity g");
        List<GuestEntity> guestEntities = query.getResultList();
        for (GuestEntity guestEntity : guestEntities) {
            guestEntity.getReservationEntities().size();
        }
        return guestEntities;
    }

    @Override
    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM GuestEntity g WHERE g.userName = :inUsername");
        query.setParameter("inUsername", username);

        try {
            GuestEntity guestEntity = (GuestEntity) query.getSingleResult();
            guestEntity.getReservationEntities().size();
            return guestEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new GuestNotFoundException("Guest with Username " + username + "does not exist!");
        }
    }

    @Override
    public GuestEntity retrieveGuestByID(String identificationNumber) {
        Query query = em.createQuery("SELECT g FROM GuestEntity g WHERE g.identificationNumber = :inIdentificationNumber");
        query.setParameter("inIdentificationNumber", identificationNumber);

        try {
            GuestEntity guestEntity = (GuestEntity) query.getSingleResult();
            guestEntity.getReservationEntities().size();
            return guestEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            return null;
        }
    }

    @Override
    public GuestEntity retrieveGuestByGuestId(Long guestId) throws GuestNotFoundException {
        GuestEntity guestEntity = em.find(GuestEntity.class, guestId);

        if (guestEntity != null) {
            
            guestEntity.getReservationEntities().size();
           
            return guestEntity;
        } else {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist!");
        }
    }

    @Override
    public void updateGuest(GuestEntity guestEntity) throws GuestNotFoundException {

        if (guestEntity.getGuestId() != null) {
            GuestEntity guestEntityToUpdate = retrieveGuestByGuestId(guestEntity.getGuestId());
            guestEntityToUpdate.setReservationEntities(guestEntity.getReservationEntities());

        } else {
            throw new GuestNotFoundException("Guest ID not provided for guest to be updated");
        }
    }

}
