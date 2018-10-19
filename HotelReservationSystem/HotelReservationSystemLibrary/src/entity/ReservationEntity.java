/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kai_n
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutDate;
    
    @Column(precision = 11, scale = 2)
    private BigDecimal reservationAmount;
    private String reservationType;

    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private GuestEntity guestEntity;
    
    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private PartnerEntity partnerEntity;
    
    @OneToMany (mappedBy = "reservationEntity")
    private List<ReservationLineItemEntity> reservationLineItemEntities;
    
    
    public ReservationEntity() {
        
        reservationLineItemEntities = new ArrayList<>();
        
    }

    public ReservationEntity(Date checkInDate, Date checkOutDate, BigDecimal reservationAmount, String reservationType) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationAmount = reservationAmount;
        this.reservationType = reservationType;
    }
    

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long id) {
        this.reservationId = id;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public BigDecimal getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationAmount(BigDecimal reservationAmount) {
        this.reservationAmount = reservationAmount;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public GuestEntity getGuestEntity() {
        return guestEntity;
    }

    public void setGuestEntity(GuestEntity guestEntity) {
        this.guestEntity = guestEntity;
    }

    public List<ReservationLineItemEntity> getReservationLineItemEntities() {
        return reservationLineItemEntities;
    }

    public void setReservationLineItemEntities(List<ReservationLineItemEntity> reservationLineItemEntities) {
        this.reservationLineItemEntities = reservationLineItemEntities;
    }

    public PartnerEntity getPartnerEntity() {
        return partnerEntity;
    }

    public void setPartnerEntity(PartnerEntity partnerEntity) {
        this.partnerEntity = partnerEntity;
    }
 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.reservationEntity[ id=" + reservationId + " ]";
    }
    
}
