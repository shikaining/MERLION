package util.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import util.enumeration.PaymentModeEnum;
import util.helper.BigDecimalHelper;



public class PayPalPayment
{
    public PayPalPayment()
    {
    }
 
    
    
    public String chargeVisaMasterCard(PaymentModeEnum paymentMode, String creditCardNumber, BigDecimal amount)
    {
        Random random = new Random((new Date()).getTime());        
        String paymentReferenceNumber = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        
        System.err.println("********** PayPalPayment.chargeVisaMasterCard(): " + paymentMode + "; " + creditCardNumber + "; " + BigDecimalHelper.formatCurrency(amount) + "; " + paymentReferenceNumber);
        
        return paymentReferenceNumber;
    }
}