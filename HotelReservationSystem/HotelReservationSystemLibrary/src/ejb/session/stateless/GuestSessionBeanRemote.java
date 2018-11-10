package ejb.session.stateless;

import entity.GuestEntity;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

public interface GuestSessionBeanRemote {

    GuestEntity retrieveGuestByID(String identificationNumber) throws GuestNotFoundException;

    GuestEntity createNewGuest(GuestEntity newGuestEntity);
    
    GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException;
    
    GuestEntity addUsername(GuestEntity newGuestEntity) throws GuestNotFoundException;
   
    void updateGuest(GuestEntity guestEntity) throws GuestNotFoundException;
    
}
