import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Transport {
    private static int transportCount;

    private ArrayList<Passenger> passengers;
    public enum Type {
        NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN
    };
    private Type type;
    private int maxSpeed;

    public int getNumPassengers() 
    {
        return passengers.size();
    }

    public void setNumPassengers(int numPassengers) 
    {
        for (int i = passengers.size(); i < numPassengers; i++)
        {
            passengers.add(new Passenger());
        }
        while (passengers.size() > numPassengers)
        {
            passengers.remove(passengers.size() - 1);
        }
    }

    public ArrayList<Passenger> getPassengers() 
    {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) 
    {
        this.passengers = passengers;
    }

    public int getMaxSpeed() 
    {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) 
    {
        this.maxSpeed = maxSpeed;
    }

    public Type getType() 
    {
        return type;
    }

    public void setType(Type type) 
    {
        this.type = type;
    }

    public static int getTransportCount() 
    {
        return transportCount;
    }

    public Transport()
    {
        this.passengers = new ArrayList<Passenger>();
        this.type = Type.NOT_SPECIFIED;
        transportCount++;
    }

    public Transport(ArrayList<Passenger> passengers, Transport.Type type, int maxSpeed) 
    {
        this.passengers = passengers;
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport.Type type, int maxSpeed)
    {
        this.passengers = new ArrayList<Passenger>();
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport transport)
    {
        this.passengers = (ArrayList<Passenger>)(transport.passengers.clone());
        this.type = transport.type;
        this.maxSpeed = transport.maxSpeed;
        transportCount++;
    }

    public static Transport promptCreate()
    {
        int numPassengers = Prompt.positiveInt("number of passengers");
        ArrayList<Passenger> passengers = new ArrayList<Passenger>(numPassengers);

        for (int i = 0; i < numPassengers; i++)
        {
            passengers.add(i, Passenger.promptInput());
        }

        Type type = Prompt._enum("type", Type.class);
        int maxSpeed = Prompt.positiveOrZeroInt("max speed");

        return new Transport(passengers, type, maxSpeed); 
    }

    public static Transport createRandom()
    {
        Random rand = new Random();
        int numPassengers = rand.nextInt(11);
        ArrayList<Passenger> passengers = new ArrayList<Passenger>(numPassengers);
        for (int i = 0; i < numPassengers; i++)
        {
            passengers.add(i, new Passenger());
        }
        Transport.Type[] allTypes = Transport.Type.values();
        Transport.Type type = allTypes[rand.nextInt(allTypes.length)];
        int maxSpeed = 10 + rand.nextInt(1000);

        return new Transport(passengers, type, maxSpeed);
    }

    public int getTotalPassengerWeight()
    {
        int sum = 0;
        for (Passenger p : passengers)
        {
            sum += p.getWeight();
        }
        return sum;
    }   

    public boolean compareByNumPassengersTo(Transport otherTransport)
    {
        return getNumPassengers() == otherTransport.getNumPassengers();
    }

    public static boolean compareByTotalTotalPassengerWeight(Transport a, Transport b)
    {
        return a.getTotalPassengerWeight() == b.getTotalPassengerWeight();
    }

    public void print()
    {
        System.out.printf("Number of passengers: %d\n", getNumPassengers());
        System.out.print("Passengers: \n");
        passengers.forEach(p -> p.print());
        System.out.printf("The type is: %s\n", this.type.toString());
        System.out.printf("The max speed is: %d\n", maxSpeed);
    }

    public Transport(String fileName)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(fileName);
            Scanner scanner = new Scanner(inputStream);
            int numPassengers = Integer.parseInt(scanner.nextLine());
            passengers = new ArrayList<Passenger>(numPassengers);
            for (int i = 0; i < numPassengers; i++)
            {
                passengers.add(i, Passenger.deserialize(scanner));
            }
            this.type = Transport.Type.valueOf(scanner.nextLine());
            this.maxSpeed = Integer.parseInt(scanner.nextLine());
            scanner.close();
        }
        catch (FileNotFoundException exception)
        {
            System.err.printf("File %s not found.", fileName);
        }
        catch (InputMismatchException exception)
        {
            System.err.printf("Wrong format while reading file %s.", fileName);
        }
        catch (NumberFormatException exception)
        {
            System.err.printf("Wrong format while reading file %s.", fileName);
        }
        catch (NoSuchElementException exception)
        {
            System.err.printf("Prematurely reached EOF while reading %s.", fileName);
        }
    }

    public void serialize(String fileName)
    {
        try
        {
            FileWriter writer = new FileWriter(fileName);
            writer.append(String.valueOf(passengers.size()));
            writer.append('\n');
            for (Passenger p : passengers)
            {
                p.serialize(writer);
            }
            writer.append(this.type.toString());
            writer.append('\n');
            writer.append(String.valueOf(maxSpeed));
            writer.close();
        }
        catch (FileNotFoundException exception)
        {
            System.err.printf("The file %s is a directory. Couldn't serialize.", fileName);
        }
        catch (IOException exception)
        {
            System.err.printf("Error while writing to the file %s.", fileName);
        }
    }
}