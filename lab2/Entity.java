import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

abstract class Entity {
    public int id = 0;
    private static int currentId = 0;
    protected static Random random = new Random(69); /*new Random();*/
    protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

    public final void serializeTo(String file) {
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