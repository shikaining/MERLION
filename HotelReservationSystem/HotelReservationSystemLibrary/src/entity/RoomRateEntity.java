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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.rateTypeEnum;


@Entity
public class RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    
    @Column(unique = true, nullable = false, length = 20)
    private String name;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal ratePerNight;
    
    @Enumerated(EnumType.STRING)
    private rateTypeEnum rateType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date validityStart;   
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date validityEnd;  
    
    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomTypeEntity;
    
    @OneToMany (mappedBy = "roomRateEntity")
    private List<ReservedNightEntity> reservedNightEntities;

    public RoomRateEntity() {
        reservedNightEntities = new ArrayList<>();
    }

    /*
    promo over normal
    peak over normal
    promo over peak, normal
    peak over promo
    if a certain room type has no published rate then cannot walk-in reservation
    if no normal then cannot have normal reservation
    */

    public RoomRateEntity(String name, BigDecimal ratePerNight, rateTypeEnum rateType, Date validityStart, Date validityEnd, RoomTypeEntity roomTypeEntity) {
        this.name = name;
        this.ratePerNight = ratePerNight;
        this.rateType = rateType;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
        this.roomTypeEntity = roomTypeEntity;
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

    public List<ReservedNightEntity> getReservedNightEntities() {
        return reservedNightEntities;
    }

    public void setReservedNightEntities(List<ReservedNightEntity> reservedNightEntities) {
        this.reservedNightEntities = reservedNightEntities;
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
