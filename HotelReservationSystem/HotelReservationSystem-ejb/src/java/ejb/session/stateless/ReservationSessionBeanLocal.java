package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;

public interface ReservationSessionBeanLocal {
    
    ReservationEntity createNewReservation(ReservationEntity newReservationEntity);
    
    List<ReservationEntity> retrieveAllReservations();
    
}
