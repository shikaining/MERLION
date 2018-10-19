package ejb.session.stateless;

import java.math.BigDecimal;
import util.enumeration.PaymentModeEnum;
import util.exception.InvalidPaymentModeException;



public interface CreditCardProcessingSessionBeanLocal
{    
    public String chargeCreditCard(PaymentModeEnum paymentMode, String creditCardNumber, BigDecimal amount) throws InvalidPaymentModeException;
}