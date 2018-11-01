/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import util.exception.GuestNotFoundException;


public interface GuestSessionBeanRemote {

    GuestEntity retrieveGuestByID(String identificationNumber) throws GuestNotFoundException;

    GuestEntity createNewGuest(GuestEntity newGuestEntity);

    void updateGuest(GuestEntity guestEntity) throws GuestNotFoundException;
    
}
