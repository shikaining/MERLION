/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.rateTypeEnum;


@Entity
public class RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    //name is e.g. deluxe normal
    private String name;
    @Column(precision = 11, scale = 2)
    private BigDecimal ratePerNight;
    //rateType is e.g. normal
    private rateTypeEnum rateType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date validityStart;   
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date validityEnd;  
    //need to set validity period if applicable-**
    
    //roomType will be e.g. deluxe
    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomTypeEntity;

    public RoomRateEntity() {
    }

    public RoomRateEntity(String name, BigDecimal ratePerNight, rateTypeEnum rateType, Date validityStart, Date validityEnd) {
        this();
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.rateType = rateType;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
    }
 
    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long id) {
        this.roomRateId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public rateTypeEnum getRateType() {
        return rateType;
    }

    public void setRateType(rateTypeEnum rateType) {
        this.rateType = rateType;
    }

    public Date getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(Date validityStart) {
        this.validityStart = validityStart;
    }

    public Date getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }

    public RoomTypeEntity getRoomTypeEntity() {
        return roomTypeEntity;
    }

    public void setRoomTypeEntity(RoomTypeEntity roomTypeEntity) {
        this.roomTypeEntity = roomTypeEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomRateEntity)) {
            return false;
        }
        RoomRateEntity other = (RoomRateEntity) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.roomRateEntity[ id=" + roomRateId + " ]";
    }
    
}
