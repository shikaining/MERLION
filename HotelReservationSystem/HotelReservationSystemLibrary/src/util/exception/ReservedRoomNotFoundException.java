package util.exception;



public class ReservedRoomNotFoundException extends Exception 
{
    public ReservedRoomNotFoundException() 
    {
    }

    
    
    public ReservedRoomNotFoundException(String msg) 
    {
        super(msg);
    }
}