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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author kai_n
 */
@Entity
public class ReservedRoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservedRoomId;
    
    @ManyToOne 
    private RoomTypeEntity roomTypeEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ReservationEntity reservationEntity;
    
    @OneToMany (mappedBy = "reservedRoomEntity")
    private List<ReservedNightEntity> reservedNightEntities;

    @ManyToOne (optional = true)
    @JoinColumn(nullable = true)
    private RoomEntity roomEntity;
    
    public ReservedRoomEntity() {
        reservedNightEntities = new ArrayList<>();
    }
    
    
    public Long getReservedRoomId() {
        return reservedRoomId;
    }

    public void setReservedRoomId(Long reservedRoomId) {
        this.reservedRoomId = reservedRoomId;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public ReservationEntity getReservationEntity() {
        return reservationEntity;
    }

    public void setReservationEntity(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
    }

    public List<ReservedNightEntity> getReservedNightEntities() {
        return reservedNightEntities;
    }

    public void setReservedNightEntities(List<ReservedNightEntity> reservedNightEntities) {
        this.reservedNightEntities = reservedNightEntities;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }
   
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservedRoomId != null ? reservedRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservedRoomEntity)) {
            return false;
        }
        ReservedRoomEntity other = (ReservedRoomEntity) object;
        if ((this.reservedRoomId == null && other.reservedRoomId != null) || (this.reservedRoomId != null && !this.reservedRoomId.equals(other.reservedRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservedRoomId[ id=" + reservedRoomId + " ]";
    }
    
}
