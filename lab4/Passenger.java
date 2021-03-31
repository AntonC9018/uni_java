import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Passenger 
{
    private String name;
    private int weight;
    private int height;

    public Passenger(String name, int weight, int height)
    {
        this.name = name;
        this.weight = weight;
        this.height = height;
    }

    private static String[] namesPool = {
        "John", "Ben", "Jim", "Kate"
    };

    private static Random random = new Random();

    public Passenger()
    {
        this.name = namesPool[random.nextInt(namesPool.length)];
        this.weight = random.nextInt(100) + 5;
        this.height = random.nextInt(100) + 5;
    }

    public static Passenger promptInput()
    {
        String name = Prompt.string("name", 3);
        int weight = Prompt.int_("weight", 5, 200);
        int height = Prompt.int_("height", 5, 200);

        return new Passenger(name, weight, height);
    }

    public void print()
    {
        System.out.printf("Name: %s, Weight: %d, Height: %d.\n", name, weight, height);
    }

    public void serialize(FileWriter writer) throws IOException
    {
        writer.append(name);
        writer.append("\n");
        writer.append(String.valueOf(weight));
        writer.append("\n");
        writer.append(String.valueOf(height));
        writer.append("\n");
    }

    public static Passenger deserialize(Scanner scanner) throws NumberFormatException
    {
        String name = scanner.nextLine();
        int weight = Integer.parseInt(scanner.nextLine());
        int height = Integer.parseInt(scanner.nextLine());

        return new Passenger(name, weight, height);
    }

    public String getName() 
    {
        return name;
    }
    public int getWeight() 
    {
        return weight;
    }
    public int getHeight() 
    {
        return height;
    }
    public void setWeight(int weight) 
    {
        this.weight = weight;
    }
    public void setHeight(int height) 
    {
        this.height = height;
    }
    public void setName(String name) 
    {
        this.name = name;
    }
}
