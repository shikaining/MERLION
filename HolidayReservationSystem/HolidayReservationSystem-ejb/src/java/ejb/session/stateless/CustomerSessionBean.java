package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;



@Stateless
@Local(CustomerSessionBeanLocal.class)
@Remote(CustomerSessionBeanRemote.class)

public class CustomerSessionBean implements CustomerSessionBeanLocal, CustomerSessionBeanRemote 
{
    //private List<Customer> customers;
    @PersistenceContext(unitName = "HolidayReservationSystem-ejbPU")
    private EntityManager em;
    
    
    public CustomerSessionBean() 
    {
        
    }
    
    @Override
    public Customer createCustomer(Customer newCustomer) {
        
        em.persist(newCustomer);
        em.flush();
        
        return newCustomer;
    }
    
    
    @Override
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        if (customer!=null) {
            return customer;
        }
        else {
            throw new CustomerNotFoundException("Customer Id "+ customerId +" does not exist!");
        }
    }
    
    @Override
    public Customer retrieveCustomerByEmail (String email) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException|NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException ("Customer with Email "+email+"does not exist!");
        }
    }
    
    
    
    @Override
    public Customer login(String email, String password) throws InvalidLoginCredentialException
    {
            Customer customer = null;
            try {
            customer = retrieveCustomerByEmail(email);
            } catch (CustomerNotFoundException ex) {
          
            }
            if(customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                return customer;
            }
            else {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
    }
}