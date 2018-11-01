package util.exception;



public class ReservationNotFoundException extends Exception 
{
    public ReservationNotFoundException() 
    {
    }

    
    
    public ReservationNotFoundException(String msg) 
    {
        super(msg);
    }
}