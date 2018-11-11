/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

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
    
    @Column(unique = true, nullable = false, length = 16)
    private String name;
    @Column(length = 30, nullable = true)
    private String description;
    @Column(length = 16)
    private String size;
    @Column(length = 16)
    private String bed;
    @Min(1)
    private Integer capacity;
   
    private Integer numOfRooms;
    private List<String> amenities;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<RoomRateEntity> roomRateEntities;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<RoomEntity> roomEntities;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private List<ReservedRoomEntity> reservedRoomEntities;

    public RoomTypeEntity() {
        amenities = new ArrayList<>();
        roomRateEntities = new ArrayList<>();
        roomEntities = new ArrayList<>();
        reservedRoomEntities = new ArrayList<>();
    }

    public RoomTypeEntity(String name, String description, String size, String bed, Integer capacity, Integer numOfRooms, List<String> amenities) {
        this();
        this.name = name;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.numOfRooms = numOfRooms;
        this.amenities = amenities;
    }
    

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(Integer numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<RoomRateEntity> getRoomRateEntities() {
        return roomRateEntities;
    }

    public void setRoomRateEntities(List<RoomRateEntity> roomRateEntities) {
        this.roomRateEntities = roomRateEntities;
    }

    public List<RoomEntity> getRoomEntities() {
        return roomEntities;
    }

    public void setRoomEntities(List<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }

    public List<ReservedRoomEntity> getReservedRoomEntities() {
        return reservedRoomEntities;
    }

    public void setReservedRoomEntities(List<ReservedRoomEntity> reservedRoomEntities) {
        this.reservedRoomEntities = reservedRoomEntities;
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
