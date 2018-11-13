package ejb.session.stateful;

import java.math.BigDecimal;



public interface HolidayReservationSessionBeanRemote 
{
    void remove();
    
    BigDecimal getTotalAmount();
    
}