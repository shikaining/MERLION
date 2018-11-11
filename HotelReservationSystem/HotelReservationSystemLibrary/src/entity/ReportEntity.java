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

/**
 *
 * @author kai_n
 */
@Entity
public class ReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    private List<String> typeIException;
    private List<String> typeIIException;

    public ReportEntity() {
        typeIException = new ArrayList<>();
        typeIIException = new ArrayList<>();

    }

    public ReportEntity(List<String> exceptionOne, List<String> exceptionTwo) {
        this.typeIException = exceptionOne;
        this.typeIIException = exceptionTwo;
    }
    

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportId fields are not set
        if (!(object instanceof ReportEntity)) {
            return false;
        }
        ReportEntity other = (ReportEntity) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReportEntity[ id=" + reportId + " ]";
    }

}
