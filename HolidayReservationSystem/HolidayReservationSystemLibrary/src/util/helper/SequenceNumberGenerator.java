package util.helper;



public class SequenceNumberGenerator 
{
    private static Long SEQUENCE_NUMBER = 0l;
    
    
    
    public static Long getNextSequenceNumber()
    {
        return ++SEQUENCE_NUMBER;
    }
}
