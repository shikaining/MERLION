package ejb.session.stateless;

import entity.Customer;
import util.exception.CustomerNotFoundException;



public interface CustomerSessionBeanLocal
{    
    Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;

    Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    Customer createCustomer(Customer newCustomer);
}