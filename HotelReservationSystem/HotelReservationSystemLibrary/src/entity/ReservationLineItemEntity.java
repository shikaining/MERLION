/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kai_n
 */
@Entity
public class ReservationLineItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationLineItemId;

    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutDate;
    private int quantityOfRooms;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomTypeEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ReservationEntity reservationEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomRateEntity roomRateEntity;

    public ReservationLineItemEntity() {
    }

    public ReservationLineItemEntity(Long reservationId, Date checkInDate, Date checkOutDate, int quantityOfRooms) {
        this.reservationId = reservationId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.quantityOfRooms = quantityOfRooms;
    }
    
    
    public Long getReservationLineItemId() {
        return reservationLineItemId;
    }

    public void setReservationLineItemId(Long id) {
        this.reservationLineItemId = id;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setQuantityOfRooms(int quantityOfRooms) {
        this.quantityOfRooms = quantityOfRooms;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    public void setReservationEntity(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
    }

    public void setRoomRateEntity(RoomRateEntity roomRateEntity) {
        this.roomRateEntity = roomRateEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationLineItemId != null ? reservationLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationLineItemEntity)) {
            return false;
        }
        ReservationLineItemEntity other = (ReservationLineItemEntity) object;
        if ((this.reservationLineItemId == null && other.reservationLineItemId != null) || (this.reservationLineItemId != null && !this.reservationLineItemId.equals(other.reservationLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationLineItemEntity[ id=" + reservationLineItemId + " ]";
    }
    
}
