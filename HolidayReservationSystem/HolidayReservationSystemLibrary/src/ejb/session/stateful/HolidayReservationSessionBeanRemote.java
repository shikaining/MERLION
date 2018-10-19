package ejb.session.stateful;

import entity.ItineraryItem;
import entity.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.enumeration.PaymentModeEnum;
import util.exception.CheckoutException;



public interface HolidayReservationSessionBeanRemote 
{
    void remove();
    
    List<ItineraryItem> searchHolidays(Date departureDate, Date returnDate, String departureCity, String destinationCity, Integer numberOfTravellers);

    BigDecimal getTotalAmount();

    Transaction reserveHoliday(Long customerId, PaymentModeEnum paymentMode, String creditCardNumber) throws CheckoutException;    
}