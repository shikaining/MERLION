/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author kai_n
 */
@Entity
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    //e.g. premier
    private String name;
    private String description;
    //large,medium etc
    private String size;
    //eg.king,queen
    private String bed;
    //5 people eg
    private int capacity;
    private List<String> amenities;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<RoomRateEntity> roomRateEntities;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<RoomEntity> roomEntities;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<ReservationLineItemEntity> reservationLineItemEntities;
    

    public RoomTypeEntity() {
        roomRateEntities = new ArrayList<>();
        roomEntities = new ArrayList<>();
        reservationLineItemEntities = new ArrayList<>();
    }

    public RoomTypeEntity(String name, String description, String size, String bed, int capacity, List<String> amenities) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
    }
    
    

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long id) {
        this.roomTypeId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<RoomEntity> getRoomEntities() {
        return roomEntities;
    }

    public void setRoomEntities(List<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }

    public List<RoomRateEntity> getRoomRateEntities() {
        return roomRateEntities;
    }

    public void setRoomRateEntities(List<RoomRateEntity> roomRateEntities) {
        this.roomRateEntities = roomRateEntities;
    }

    public List<ReservationLineItemEntity> getReservationLineItemEntities() {
        return reservationLineItemEntities;
    }

    public void setReservationLineItemEntities(List<ReservationLineItemEntity> reservationLineItemEntities) {
        this.reservationLineItemEntities = reservationLineItemEntities;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.roomTypeEntity[ id=" + roomTypeId + " ]";
    }
    
}
