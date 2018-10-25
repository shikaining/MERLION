package ejb.session.stateless;

import entity.RoomRateEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomRateException;
import util.exception.RoomRateNotFoundException;


@Stateless
@Local (RoomRateSessionBeanLocal.class)
@Remote (RoomRateSessionBeanRemote.class)
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRateEntity)
    {
        em.persist(newRoomRateEntity);
        em.flush();
        
        return newRoomRateEntity;
    }
    
 
    @Override
    public List<RoomRateEntity> retrieveAllRoomRates()
    {
        Query query = em.createQuery("SELECT rr FROM RoomRateEntity rr");
        List<RoomRateEntity> roomRateEntities = new ArrayList<>();
        roomRateEntities = query.getResultList();
   
        return roomRateEntities;
    }
    
    @Override
    public RoomRateEntity retrieveRoomRateByName(String name) throws RoomRateNotFoundException
    {
        Query query = em.createQuery("SELECT rr FROM RoomRateEntity rr WHERE rr.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (RoomRateEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new RoomRateNotFoundException("Room Rate Name " + name + " does not exist!");
        }
    }
   
    @Override
    public void updateRoomRate(RoomRateEntity roomRateEntity) throws RoomRateNotFoundException
    {
        
        if(roomRateEntity.getRoomRateId()!= null)
        {
            RoomRateEntity roomRateEntityToUpdate = retrieveRoomRateByRoomRateId(roomRateEntity.getRoomRateId());
            
                roomRateEntityToUpdate.setName(roomRateEntity.getName());
                roomRateEntityToUpdate.setRatePerNight(roomRateEntity.getRatePerNight());
                roomRateEntityToUpdate.setRateType(roomRateEntity.getRateType());
                roomRateEntityToUpdate.setRoomTypeEntity(roomRateEntity.getRoomTypeEntity());
                roomRateEntityToUpdate.setValidityStart(roomRateEntity.getValidityStart());
                roomRateEntityToUpdate.setValidityStart(roomRateEntity.getValidityEnd());
         
        }
        else
        {
            throw new RoomRateNotFoundException("Room Rate ID not provided for room rate to be updated");
        }
    }
    
    @Override
    public RoomRateEntity retrieveRoomRateByRoomRateId(Long roomRateId) throws RoomRateNotFoundException
    {
        RoomRateEntity roomRateEntity = em.find(RoomRateEntity.class, roomRateId);
        
        if(roomRateEntity != null)
        {
            return roomRateEntity;
        }
        else
        {
            throw new RoomRateNotFoundException("Room Rate ID " + roomRateId + " does not exist!");
        }
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, DeleteRoomRateException
    {
        RoomRateEntity roomRateEntityToRemove = retrieveRoomRateByRoomRateId(roomRateId);
        if(roomRateEntityToRemove.getReservationLineItemEntities().isEmpty())
        {
             em.remove(roomRateEntityToRemove);
        }
        else
        {
            throw new DeleteRoomRateException("Room Rate ID " + roomRateId + " is associated with existing reservation(s) and cannot be deleted!");
        }
    }
    
}
