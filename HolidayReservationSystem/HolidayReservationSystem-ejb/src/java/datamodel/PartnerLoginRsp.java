package datamodel;

import entity.PartnerEntity;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PartnerLoginRsp {

    private PartnerEntity partner;

    public PartnerLoginRsp() {
    }

    public PartnerLoginRsp(PartnerEntity partner) {
        this.partner = partner;
    }

    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

}
