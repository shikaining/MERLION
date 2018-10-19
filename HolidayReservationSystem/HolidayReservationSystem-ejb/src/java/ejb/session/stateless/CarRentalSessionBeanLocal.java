package ejb.session.stateless;

import entity.ItineraryItem;
import java.util.Date;
import java.util.List;



public interface CarRentalSessionBeanLocal 
{
    public List<ItineraryItem> searchRentalCars(Date departureFlightLanding, Date returnFlightTakeoff, String departureCity, String destinationCity);
}