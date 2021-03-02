public class EntityIdInitializedException extends Exception 
{ 
    private static final long serialVersionUID = 1L;

    public EntityIdInitializedException(String errorMessage) 
    {
        super(errorMessage);
    }
}