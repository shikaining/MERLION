package util.exception;



public class InvalidPaymentModeException extends Exception 
{
    public InvalidPaymentModeException() 
    {
    }

    
    
    public InvalidPaymentModeException(String msg) 
    {
        super(msg);
    }
}