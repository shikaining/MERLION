package util.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import util.enumeration.PaymentModeEnum;
import util.helper.BigDecimalHelper;



public class ThirdPartyGatewayPayment 
{
    public ThirdPartyGatewayPayment()
    {
    }
    
    
    
    public String chargeAmex(PaymentModeEnum paymentMode, String creditCardNumber, BigDecimal amount)
    {
        Random random = new Random((new Date()).getTime());        
        String paymentReferenceNumber = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        
        System.err.println("********** ThirdPartyGatewayPayment.chargeAmex(): " + paymentMode + "; " + creditCardNumber + "; " + BigDecimalHelper.formatCurrency(amount) + "; " + paymentReferenceNumber);
        
        return paymentReferenceNumber;
    }
}
