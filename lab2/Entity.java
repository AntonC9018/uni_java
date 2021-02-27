import java.util.Random;

abstract class Entity
{
    public int id = 0;
    private static int currentId = 0;
    protected static Random random = new Random(69);

    public final int getId()
    {
        return id;
    }

    protected final void setId(int id)
    {
        this.id = id;
    }

    public static void autoId(Entity entity)
    {
        currentId++;
        entity.setId(currentId);
    }

    public abstract void promptInput();
    public abstract void randomize();
    
    public final void serialize(String file)
    {

    }
}