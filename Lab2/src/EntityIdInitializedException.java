public class EntityIdInitializedException extends RuntimeException 
{ 
    private static final long serialVersionUID = 1L;

    public EntityIdInitializedException(String errorMessage) 
    {
        super(errorMessage);
    }
}