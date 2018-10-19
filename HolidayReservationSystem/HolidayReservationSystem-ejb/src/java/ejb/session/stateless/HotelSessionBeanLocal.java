package ejb.session.stateless;

import entity.ItineraryItem;
import java.util.Date;
import java.util.List;



public interface HotelSessionBeanLocal 
{
    public List<ItineraryItem> searchHotelRooms(Date departureFlightLanding, Date returnFlightTakeoff, String departureCity, String destinationCity);
}