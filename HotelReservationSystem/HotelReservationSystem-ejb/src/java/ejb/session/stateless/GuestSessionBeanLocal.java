/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import java.util.List;
import util.exception.GuestNotFoundException;


public interface GuestSessionBeanLocal {

    GuestEntity createNewCustomer(GuestEntity newGuestEntity);

    List<GuestEntity> retrieveAllGuests();

    GuestEntity retrieveGuestsByUsername(String username) throws GuestNotFoundException;
    
}
