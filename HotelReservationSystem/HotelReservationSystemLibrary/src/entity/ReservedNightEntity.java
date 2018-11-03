package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ReservedNightEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservedNightId;
    @Column(precision = 11, scale = 2)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ReservedRoomEntity reservedRoomEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomRateEntity roomRateEntity;
    
    public ReservedNightEntity() {
        
    }

    public ReservedNightEntity(BigDecimal amount) {
        this.amount = amount;
    }
        public Long getReservedNightId() {
        return reservedNightId;
    }

    public void setReservedNightId(Long reservedNightId) {
        this.reservedNightId = reservedNightId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ReservedRoomEntity getReservedRoomEntity() {
        return reservedRoomEntity;
    }

    public void setReservedRoomEntity(ReservedRoomEntity reservedRoomEntity) {
        this.reservedRoomEntity = reservedRoomEntity;
    }

    public RoomRateEntity getRoomRateEntity() {
        return roomRateEntity;
    }

    public void setRoomRateEntity(RoomRateEntity roomRateEntity) {
        this.roomRateEntity = roomRateEntity;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservedNightId != null ? reservedNightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservedNightEntity)) {
            return false;
        }
        ReservedNightEntity other = (ReservedNightEntity) object;
        if ((this.reservedNightId == null && other.reservedNightId != null) || (this.reservedNightId != null && !this.reservedNightId.equals(other.reservedNightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservedNightEntity[ id=" + reservedNightId + " ]";
    }
    
}
