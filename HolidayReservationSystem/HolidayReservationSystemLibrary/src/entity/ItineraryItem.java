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
import javax.persistence.ManyToOne;

/**
 *
 * @author kai_n
 */
@Entity
public class ItineraryItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itineraryItemId;
    private Integer sequenceNumber;
    private Date dateTime;
    private String activity;

    @ManyToOne
    private Transaction transaction;
    
    public ItineraryItem() {
    }

    public ItineraryItem(Integer sequenceNumber, Date dateTime, String activity) {
        this();
        this.sequenceNumber = sequenceNumber;
        this.dateTime = dateTime;
        this.activity = activity;
    }

    public Long getItineraryItemId() {
        return itineraryItemId;
    }

    public void setItineraryItemId(Long itineraryItemId) {
        this.itineraryItemId = itineraryItemId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itineraryItemId != null ? itineraryItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItineraryItem)) {
            return false;
        }
        ItineraryItem other = (ItineraryItem) object;
        if ((this.itineraryItemId == null && other.itineraryItemId != null) || (this.itineraryItemId != null && !this.itineraryItemId.equals(other.itineraryItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ItineraryItem[ id=" + itineraryItemId + " ]";
    }
    
}
