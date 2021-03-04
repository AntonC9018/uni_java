import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

abstract class Entity 
{
    private int id;
    private static int currentId = 0;
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected final static Random random = new Random(69); /*new Random();*/

    public final int getId() 
    {
        return id;
    }

    protected final void setId(int id) throws EntityIdInitializedException
    {
        if (this.id != 0)
        {
            throw new EntityIdInitializedException("The id has been already set");
        }
        this.id = id;
    }

    public static void autoId(Entity entity) 
    {
        currentId++;
        try
        {
            entity.setId(currentId);
        }
        catch(EntityIdInitializedException exception)
        {
            System.out.println("Sorry, cannot initialize the id twice.");
        }
    }

    public abstract void promptInput();

    public abstract void randomize();

    public final void serializeTo(String file) 
    {
        try 
        {
            FileWriter stream = new FileWriter(file);
            serializeTo(stream);
            stream.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public final void print()
    {
        serializeTo(System.out);
        System.out.println();
    }

    private final void serializeTo(Appendable stream)
    {
        gson.toJson(this, stream); 
    }
}