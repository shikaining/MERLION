package ejb.session.stateless;

import java.math.BigDecimal;
import javax.ejb.Local;
import javax.ejb.Stateless;
import util.enumeration.PaymentModeEnum;
import util.exception.InvalidPaymentModeException;
import util.payment.PayPalPayment;
import util.payment.ThirdPartyGatewayPayment;



@Stateless
@Local(CreditCardProcessingSessionBeanLocal.class)

public class CreditCardProcessingSessionBean implements CreditCardProcessingSessionBeanLocal 
{
    @Override
    public String chargeCreditCard(PaymentModeEnum paymentMode, String creditCardNumber, BigDecimal amount) throws InvalidPaymentModeException
    {
        if(paymentMode.equals(PaymentModeEnum.VISA) || paymentMode.equals(PaymentModeEnum.MASTERCARD))
        {
            PayPalPayment payPalPayment = new PayPalPayment();
            
            return payPalPayment.chargeVisaMasterCard(paymentMode, creditCardNumber, amount);
        }
        else if(paymentMode.equals(PaymentModeEnum.AMEX))
        {
            ThirdPartyGatewayPayment thirdPartyGatewayPayment = new ThirdPartyGatewayPayment();
            
            return thirdPartyGatewayPayment.chargeAmex(paymentMode, creditCardNumber, amount);
        }
        else
        {
            throw new InvalidPaymentModeException("Unsupported payment mode: " + paymentMode);
        }
    }
}