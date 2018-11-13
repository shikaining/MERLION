package ejb.session.stateful;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
@Remote(HolidayReservationSessionBeanRemote.class)

//this is a stateful session bean! Need to maintain the amount of money & itinerary items
public class HolidayReservationSessionBean implements HolidayReservationSessionBeanRemote {

    private Date departureDate;
    private Date returnDate;
    private String departureCity;
    private String destinationCity;
    private Integer numberOfTravellers;

    private BigDecimal totalAmount;

    @Remove
    @Override
    public void remove() {
        // Do nothing
    }

    @Override
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
