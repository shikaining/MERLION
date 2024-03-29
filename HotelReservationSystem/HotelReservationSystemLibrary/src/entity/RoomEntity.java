/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.roomStatusEnum;

@Entity
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @Column(unique = true)
    private String roomNumber;
    //available, unavailable, allocated, cleaning
    @Enumerated(EnumType.STRING)
    private roomStatusEnum status;
    
    @ManyToOne (optional = false)
    @JoinColumn (nullable = false)
    private RoomTypeEntity roomTypeEntity;
    
    @OneToOne
    private ReservedRoomEntity reservedRoomEntity;

    public RoomEntity() {
    }

    public RoomEntity(String roomNumber, roomStatusEnum status) {
        this();
        this.roomNumber = roomNumber;
        this.status = status;
    }
    
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long id) {
        this.roomId = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public roomStatusEnum getStatus() {
        return status;
    }

    public void setStatus(roomStatusEnum status) {
        this.status = status;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public ReservedRoomEntity getReservedRoomEntity() {
        return reservedRoomEntity;
    }

    public void setReservedRoomEntity(ReservedRoomEntity reservedRoomEntity) {
        this.reservedRoomEntity = reservedRoomEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.roomEntity[ id=" + roomId + " ]";
    }
    
}
