package ejb.session.stateless;

import entity.ItineraryItem;
import java.util.Date;
import java.util.List;



public interface FlightTicketSessionBeanLocal
{    
    public List<ItineraryItem> searchFlights(Date departureDate, Date returnDate, String departureCity, String destinationCity);
}