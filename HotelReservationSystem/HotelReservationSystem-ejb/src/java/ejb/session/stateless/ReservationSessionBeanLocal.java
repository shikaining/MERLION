
package ejb.session.stateless;

import entity.ReservationEntity;
import util.exception.ReservationNotFoundException;

public interface ReservationSessionBeanLocal {

    ReservationEntity retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    
}
