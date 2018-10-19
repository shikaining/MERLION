package ejb.session.stateless;

import entity.Customer;
import entity.ItineraryItem;
import entity.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.PaymentModeEnum;
import util.exception.CheckoutException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidPaymentModeException;
import util.helper.SequenceNumberGenerator;



@Stateless
@Local(CheckoutSessionBeanLocal.class)

//customer & creditcard processing sessionbeans
public class CheckoutSessionBean implements CheckoutSessionBeanLocal
{
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    private CreditCardProcessingSessionBeanLocal creditCardProcessingSessionBeanLocal;
 
    @PersistenceContext(unitName = "HolidayReservationSystem-ejbPU")
    private EntityManager em;
    
    
   
    @Override
    public Transaction createTransaction(Transaction newTransaction) {
        
        em.persist(newTransaction);
        em.flush();
        
        return newTransaction;
    }
    
    @Override
    public ItineraryItem createItineraryItem(ItineraryItem newItineraryItem) {
        
        em.persist(newItineraryItem);
        em.flush();
        
        return newItineraryItem;
    }
    
    @Override
    public void updateItineraryItem(ItineraryItem itineraryItem)
    {
        em.merge(itineraryItem);
    }
//    
    public Transaction checkout(Long customerId, List<ItineraryItem> itineraryItems, PaymentModeEnum paymentMode, String creditCardNumber, BigDecimal totalAmount) throws CheckoutException
    {
        //logged in customer
        //arraylist created
        //items are available in arraylist
        System.out.println("Step Three");
        if(customerId != null && itineraryItems != null && !itineraryItems.isEmpty())
        {
            System.out.println("Step Four");
            try 
            {
                System.out.println("Step Five");
                Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
                System.out.println("Step Six");
                String paymentReferenceNumber = creditCardProcessingSessionBeanLocal.chargeCreditCard(paymentMode, creditCardNumber, totalAmount);
                System.out.println("Step Seven");
                for(ItineraryItem itineraryItem:itineraryItems)
                {
                    itineraryItem.setItineraryItemId(SequenceNumberGenerator.getNextSequenceNumber());
                    createItineraryItem(itineraryItem);
                }
                
                Transaction transaction = new Transaction(SequenceNumberGenerator.getNextSequenceNumber(), totalAmount, new Date(), customer);
                transaction.setItineraryItems(itineraryItems);
                transaction.setCustomer(customer);
                createTransaction(transaction);
                
                for(ItineraryItem itineraryItem:itineraryItems)
                {
                    itineraryItem.setTransaction(transaction);
                    updateItineraryItem(itineraryItem);
                    
                }
                
                return transaction;
            } 
            catch (CustomerNotFoundException | InvalidPaymentModeException ex) 
            {
                throw new CheckoutException(ex.getMessage());
            }
        }
        else
        {
            throw new CheckoutException("Missing customer data or no holiday to checkout");
        }
    }
}