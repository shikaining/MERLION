package ejb.session.stateless;

import entity.ItineraryItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.ejb.Local;
import javax.ejb.Stateless;



@Stateless
@Local(FlightTicketSessionBeanLocal.class)

public class FlightTicketSessionBean implements FlightTicketSessionBeanLocal 
{
    @Override
    public List<ItineraryItem> searchFlights(Date departureDate, Date returnDate, String departureCity, String destinationCity)
    {
        //random date random time
        Random random = new Random((new Date()).getTime());
        
        List<ItineraryItem> flightTickets = new ArrayList<>();
        //this arraylist stores the itinerary items related to flight tickets
        
        Date departureFlightTakeoff = departureDate;
        //generate hours between 0 to 24
        departureFlightTakeoff.setHours(random.nextInt(24));
        //generate minutes between 0 to 60
        departureFlightTakeoff.setMinutes(((random.nextInt(60) + 4) / 5) * 5);
        
        //generate a calendar
        GregorianCalendar departureFlightLandingCalendar = new GregorianCalendar();
        //add departure time to the calendar
        departureFlightLandingCalendar.setTime(departureFlightTakeoff);
        
        departureFlightLandingCalendar.add(GregorianCalendar.HOUR_OF_DAY, random.nextInt(10) + 1);        
        Date departureFlightLanding = departureFlightLandingCalendar.getTime();
        
        Date returnFlightLanding = returnDate;
        returnFlightLanding.setHours(random.nextInt(24));
        returnFlightLanding.setMinutes(((random.nextInt(60) + 4) / 5) * 5);
        
        GregorianCalendar returnFlightTakeoffCalendar = new GregorianCalendar();
        returnFlightTakeoffCalendar.setTime(returnFlightLanding);
        returnFlightTakeoffCalendar.add(GregorianCalendar.HOUR_OF_DAY, (random.nextInt(10) + 1) * -1);
        Date returnFlightTakeoff = returnFlightTakeoffCalendar.getTime();
        
        String departureFlightNumber = String.valueOf((char)(random.nextInt(26) + 'A')) + String.valueOf((char)(random.nextInt(26) + 'A'));
        String returnFlightNumber = departureFlightNumber;
        departureFlightNumber = departureFlightNumber + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        returnFlightNumber = returnFlightNumber + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        
        //add itinerary items to the list of flighttickets
        //format: ItineraryItem(Integer sequenceNumber, Date dateTime, String activity)
        flightTickets.add(new ItineraryItem(1, departureFlightTakeoff, "Board flight " + departureFlightNumber + " from " + departureCity + " Airport"));
        flightTickets.add(new ItineraryItem(2, departureFlightLanding, "Flight " + departureFlightNumber + " lands at " + destinationCity + " Airport"));
        flightTickets.add(new ItineraryItem(3, returnFlightTakeoff, "Board flight " + returnFlightNumber + " from " + destinationCity + " Airport"));
        flightTickets.add(new ItineraryItem(4, returnFlightLanding, "Flight " + returnFlightNumber + " lands at " + departureCity + " Airport"));                
        
        return flightTickets;
    }
}