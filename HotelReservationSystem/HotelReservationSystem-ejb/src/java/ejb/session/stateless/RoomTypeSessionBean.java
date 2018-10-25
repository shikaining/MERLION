package ejb.session.stateless;

import entity.RoomTypeEntity;
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
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;


@Stateless
@Local (RoomTypeSessionBeanLocal.class)
@Remote (RoomTypeSessionBeanRemote.class)
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomTypeEntity)
    {
        em.persist(newRoomTypeEntity);
        em.flush();
        
        return newRoomTypeEntity;
    }
    
   
    @Override
    public List<RoomTypeEntity> retrieveAllRoomTypes()
    {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt");
        List<RoomTypeEntity> roomTypeEntities = new ArrayList<>();
        roomTypeEntities = query.getResultList();
        for (RoomTypeEntity roomTypeEntity: roomTypeEntities) {
            roomTypeEntity.getAmenities().size();
        }
        return roomTypeEntities;
    }
    
    @Override
    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException
    {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (RoomTypeEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new RoomTypeNotFoundException("Room Type Name " + name + " does not exist!");
        }
    }
    
    @Override
    public void updateRoomType(RoomTypeEntity roomTypeEntity) throws RoomTypeNotFoundException
    {
       
        if(roomTypeEntity.getRoomTypeId() != null)
        {
            RoomTypeEntity roomTypeEntityToUpdate = retrieveRoomTypeByRoomTypeId(roomTypeEntity.getRoomTypeId());
            if(roomTypeEntityToUpdate.getName().equals(roomTypeEntity.getName()))
            {
                
                roomTypeEntityToUpdate.setDescription(roomTypeEntity.getDescription());
                roomTypeEntityToUpdate.setSize(roomTypeEntity.getSize());
                roomTypeEntityToUpdate.setBed(roomTypeEntity.getBed());
                roomTypeEntityToUpdate.setCapacity(roomTypeEntity.getCapacity());
                roomTypeEntityToUpdate.setAmenities(roomTypeEntity.getAmenities());
               
            }
        }
        else
        {
            throw new RoomTypeNotFoundException("Room Type ID not provided for room type to be updated");
        }
    }
    
    @Override
    public RoomTypeEntity retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException
    {
        RoomTypeEntity roomTypeEntity = em.find(RoomTypeEntity.class, roomTypeId);
        
        if(roomTypeEntity != null)
        {
            return roomTypeEntity;
        }
        else
        {
            throw new RoomTypeNotFoundException("Room Type ID " + roomTypeId + " does not exist!");
        }
    }

    @Override
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException, DeleteRoomTypeException
    {
        RoomTypeEntity roomTypeEntityToRemove = retrieveRoomTypeByRoomTypeId(roomTypeId);
        if( (roomTypeEntityToRemove.getRoomEntities().isEmpty()) && (roomTypeEntityToRemove.getReservationLineItemEntities().isEmpty()))
        {
             em.remove(roomTypeEntityToRemove);
        }
        else
        {
            throw new DeleteRoomTypeException("Room Type ID " + roomTypeId + " is associated with existing room(s) and/or reservation(s) and cannot be deleted!");
        }
    }
}
