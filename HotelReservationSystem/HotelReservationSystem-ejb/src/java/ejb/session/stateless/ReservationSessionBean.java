package ejb.session.stateless;

import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;

@Stateless
@Local (ReservationSessionBeanLocal.class)
@Remote (ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {


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
    public ReservedRoomEntity createNewReservedRoom (ReservedRoomEntity newReservedRoomEntity)
    {
        em.persist(newReservedRoomEntity);
        em.flush();
        
        return newReservedRoomEntity;
    }
    
    @Override
    public ReservedNightEntity createNewReservedNight (ReservedNightEntity newReservedNightEntity)
    {
        em.persist(newReservedNightEntity);
        em.flush();
        
        return newReservedNightEntity;
    }
   
    @Override
    public List<ReservationEntity> retrieveAllReservations()
    {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r");
        
        return query.getResultList();
    }

    @Override
    public List<ReservedRoomEntity> retrieveAllReservedRooms()
    {
        Query query = em.createQuery("SELECT rr FROM ReservedRoomEntity rr");
        return query.getResultList();
    }
    
    @Override
     public ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException
    {
        ReservationEntity reservationEntity = em.find(ReservationEntity.class, reservationId);
        
        if(reservationEntity != null)
        {
            return reservationEntity;
        }
        else
        {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }
    }
     
    public void updateReservation(ReservationEntity reservationEntity) throws ReservationNotFoundException 
    {

        if(reservationEntity.getReservationId()!= null)
        {
            ReservationEntity reservationEntityToUpdate = retrieveReservationByReservationId(reservationEntity.getReservationId());
         
              
        }
        else
        {
            throw new ReservationNotFoundException("Reservation ID not provided for reservation to be updated");
        }
    }
}
