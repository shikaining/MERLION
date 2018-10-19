package ejb.session.stateless;

import entity.ItineraryItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.ejb.Local;
import javax.ejb.Stateless;



@Stateless
@Local(CarRentalSessionBeanLocal.class)

public class CarRentalSessionBean implements CarRentalSessionBeanLocal 
{
    @Override
    public List<ItineraryItem> searchRentalCars(Date departureFlightLanding, Date returnFlightTakeoff, String departureCity, String destinationCity)
    {
        //generate random what?
        Random random = new Random();
        
        List<ItineraryItem> rentalCars = new ArrayList<>();
        
        GregorianCalendar collectDateTimeCalendar = new GregorianCalendar();
        collectDateTimeCalendar.setTime(departureFlightLanding);
        collectDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, 1);
        Date collectDateTime = collectDateTimeCalendar.getTime();
        
        GregorianCalendar returnDateTimeCalendar = new GregorianCalendar();
        returnDateTimeCalendar.setTime(returnFlightTakeoff);
        returnDateTimeCalendar.add(GregorianCalendar.HOUR_OF_DAY, -1);
        Date returnDateTime = returnDateTimeCalendar.getTime();
        
        String rentalCarModel = String.valueOf((char)(random.nextInt(26) + 'A')) + String.valueOf((char)(random.nextInt(26) + 'A')) + String.valueOf((char)(random.nextInt(26) + 'A'));
        
        //2 new itinerary items 
        rentalCars.add(new ItineraryItem(1, collectDateTime, "Collect rental car model " + rentalCarModel));
        rentalCars.add(new ItineraryItem(2, returnDateTime, "Return rental car model " + rentalCarModel));
        
        return rentalCars;
    }
}