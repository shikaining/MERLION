package ejb.session.stateful;

import entity.ReservationEntity;
import entity.ReservedNightEntity;
import entity.ReservedRoomEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.exception.ReservedRoomNotFoundException;


@Stateful
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
        List<ReservationEntity> reservationEntities = query.getResultList();
        for (ReservationEntity reservationEntity : reservationEntities) {
            reservationEntity.getReservedRoomEntities().size();
        }
        return reservationEntities;
    }

    @Override
    public List<ReservedRoomEntity> retrieveAllReservedRooms()
    {
        Query query = em.createQuery("SELECT rr FROM ReservedRoomEntity rr");
        List<ReservedRoomEntity> reservedRoomEntities = query.getResultList();
        for (ReservedRoomEntity reservedRoomEntity : reservedRoomEntities) {
            reservedRoomEntity.getReservedNightEntities().size();
        }
        return reservedRoomEntities;
       
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
    @Override
      public ReservedRoomEntity retrieveReservedRoomByReservationId(Long reservedRoomId) throws ReservedRoomNotFoundException
    {
        ReservedRoomEntity reservedRoomEntity = em.find(ReservedRoomEntity.class, reservedRoomId);
        
        if(reservedRoomEntity != null)
        {
            return reservedRoomEntity;
        }
        else
        {
            throw new ReservedRoomNotFoundException("ReservedRoom ID " + reservedRoomId + " does not exist!");
        }
    }
     
    @Override
    public void updateReservation(ReservationEntity reservationEntity) throws ReservationNotFoundException 
    {

        if(reservationEntity.getReservationId()!= null)
        {
            ReservationEntity reservationEntityToUpdate = retrieveReservationByReservationId(reservationEntity.getReservationId());
            reservationEntityToUpdate.setGuestEntity(reservationEntity.getGuestEntity());
         
              
        }
        else
        {
            throw new ReservationNotFoundException("Reservation ID not provided for reservation to be updated");
        }
    }
    
    @Override
    public void updateReservedRoom(ReservedRoomEntity reservedRoomEntity) throws ReservedRoomNotFoundException 
    {

        if(reservedRoomEntity.getReservedRoomId()!= null)
        {
            ReservedRoomEntity reservedRoomEntityToUpdate = retrieveReservedRoomByReservationId(reservedRoomEntity.getReservedRoomId());
         
              
        }
        else
        {
            throw new ReservedRoomNotFoundException("ReservedRoom ID not provided for reserved room to be updated");
        }
    }
   
}
