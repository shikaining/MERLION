/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author kai_n
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {
   
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    public DataInitSessionBean() {
    }
   
    @PostConstruct
    public void postConstruct() {
         try
        {
            customerSessionBeanLocal.retrieveCustomerByEmail("customer1@gmail.com");
        }
        catch(CustomerNotFoundException ex)
        {
            initializeData();
        }
    }
    
    private void initializeData()
       {
           customerSessionBeanLocal.createCustomer(new Customer("Customer1","customer1@gmail.com","password"));
           customerSessionBeanLocal.createCustomer(new Customer("Customer2","customer2@gmail.com","password"));
           customerSessionBeanLocal.createCustomer(new Customer("Customer3","customer3@gmail.com","password"));
       }
}
