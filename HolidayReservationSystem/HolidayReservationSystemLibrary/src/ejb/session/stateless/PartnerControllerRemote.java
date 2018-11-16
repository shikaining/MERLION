package ejb.session.stateless;

import Records.PartnerRecord;
import Records.RoomTypeRecord;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;

public interface PartnerControllerRemote {

    PartnerRecord partnerLoginRemote(String username, String password);

    List<RoomTypeRecord> retrieveAvailRoomTypesRemote(String username, String password, Date checkInDate, Date checkOutDate) throws DatatypeConfigurationException;

    Integer roomCountRemote(String username, String password, RoomTypeRecord roomTypeRecord, Date checkInDate, Date checkOutDate) throws DatatypeConfigurationException;

    BigDecimal calculateAmtRemote(String username, String password, RoomTypeRecord roomTypeRecord, Date checkInDate, Date checkOutDate, Boolean online) throws DatatypeConfigurationException;

}
