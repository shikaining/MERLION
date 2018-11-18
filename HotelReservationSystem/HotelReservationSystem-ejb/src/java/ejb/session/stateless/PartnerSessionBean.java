/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author kai_n
 */
@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public PartnerEntity createNewPartner(PartnerEntity newPartnerEntity) {
        em.persist(newPartnerEntity);
        em.flush();

        return newPartnerEntity;
    }

    @Override
    public List<PartnerEntity> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");

        return query.getResultList();
    }

    @Override
    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            PartnerEntity partnerEntity = retrievePartnerByUsername(username);
            System.out.println(username);
            System.out.println(password);
            if (partnerEntity.getUserName().equals(username) && partnerEntity.getPassword().equals(password)) {

                System.out.println("Partner Found (:");
                return partnerEntity;

            } else {
                throw new InvalidLoginCredentialException("Invalid login credential in partnersessionbean");
            }
        } catch (PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.userName = :inUsername");
        query.setParameter("inUsername", username);

        try {
            PartnerEntity partnerEntity = (PartnerEntity) query.getSingleResult();
            System.out.println("partner");
            System.out.println(partnerEntity.getUserName());
            System.out.println(partnerEntity.getPassword());
            partnerEntity.getReservationEntities().size();
            return partnerEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner with Username " + username + "does not exist!");
        }
    }

    @Override
    public PartnerEntity retrievePartnertByPartnerId(Long partnerId) throws PartnerNotFoundException {
        PartnerEntity partnerEntity = em.find(PartnerEntity.class, partnerId);

        if (partnerEntity != null) {

            partnerEntity.getReservationEntities().size();

            return partnerEntity;
        } else {
            throw new PartnerNotFoundException("Partner ID " + partnerId + " does not exist!");
        }
    }

}
