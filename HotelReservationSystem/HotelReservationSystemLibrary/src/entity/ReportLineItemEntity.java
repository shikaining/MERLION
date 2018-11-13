/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.exceptionTypeEnum;

/**
 *
 * @author kai_n
 */
@Entity
public class ReportLineItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportLineItemId;

    @Enumerated(EnumType.STRING)
    private exceptionTypeEnum typeEnum;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date allocationDate;

    @Column(nullable = false, length = 50)
    private String messageToGuest;
    @Column(nullable = false, length = 50)
    private String messageToAdmin;

    @Column(nullable = false)
    Long originalRoomId;
    @Column(nullable = false)
    Long originalRoomTypeId;
    @Column(nullable = true)
    Long newRoomTypeId;

    public ReportLineItemEntity() {

    }

    public ReportLineItemEntity(exceptionTypeEnum typeEnum, Date allocationDate, String messageToGuest, String messageToAdmin, Long originalRoomId, Long originalRoomTypeId) {
        this();
        this.typeEnum = typeEnum;
        this.allocationDate = allocationDate;
        this.messageToGuest = messageToGuest;
        this.messageToAdmin = messageToAdmin;
        this.originalRoomId = originalRoomId;
        this.originalRoomTypeId = originalRoomTypeId;
    }

    public Long getReportLineItemId() {
        return reportLineItemId;
    }

    public void setReportLineItemId(Long reportLineItemId) {
        this.reportLineItemId = reportLineItemId;
    }

    public exceptionTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(exceptionTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getMessageToGuest() {
        return messageToGuest;
    }

    public void setMessageToGuest(String messageToGuest) {
        this.messageToGuest = messageToGuest;
    }

    public String getMessageToAdmin() {
        return messageToAdmin;
    }

    public void setMessageToAdmin(String messageToAdmin) {
        this.messageToAdmin = messageToAdmin;
    }

    public Long getOriginalRoomId() {
        return originalRoomId;
    }

    public void setOriginalRoomId(Long originalRoomId) {
        this.originalRoomId = originalRoomId;
    }

    public Long getOriginalRoomTypeId() {
        return originalRoomTypeId;
    }

    public void setOriginalRoomTypeId(Long originalRoomTypeId) {
        this.originalRoomTypeId = originalRoomTypeId;
    }

    public Long getNewRoomTypeId() {
        return newRoomTypeId;
    }

    public void setNewRoomTypeId(Long newRoomTypeId) {
        this.newRoomTypeId = newRoomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportLineItemId != null ? reportLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportLineItemId fields are not set
        if (!(object instanceof ReportLineItemEntity)) {
            return false;
        }
        ReportLineItemEntity other = (ReportLineItemEntity) object;
        if ((this.reportLineItemId == null && other.reportLineItemId != null) || (this.reportLineItemId != null && !this.reportLineItemId.equals(other.reportLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReportEntity[ id=" + reportLineItemId + " ]";
    }

}
