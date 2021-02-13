import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Transport {
    private static int transportCount;

    private int numPassengers;
    private int[] passengerWeights;
    public enum Type {
        NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN
    };
    private Type type;
    private int maxSpeed;

    public int getNumPassengers() 
    {
        return numPassengers;
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

    // public static void setTransportCount(int transportCount) {
    //     Transport.transportCount = transportCount;
    // }

    public int[] getPassengerWeights() 
    {
        return passengerWeights;
    }

    public void setPassengerWeights(int[] passengerWeights) 
    {
        this.passengerWeights = passengerWeights;
    }

    public void setNumPassengers(int numPassengers) 
    {
        this.numPassengers = numPassengers;
    }

    public Transport()
    {
        this.passengerWeights = new int[0];
        this.type = Type.NOT_SPECIFIED;
        transportCount++;
    }

    public Transport(int numPassengers, int[] passengerWeights, Transport.Type type, int maxSpeed) {
        assert(passengerWeights.length == numPassengers);
        this.numPassengers = numPassengers;
        this.passengerWeights = passengerWeights;
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport.Type type, int maxSpeed)
    {
        this.numPassengers = 0;
        this.passengerWeights = new int[0];
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport transport)
    {
        this.numPassengers = transport.numPassengers;
        this.passengerWeights = transport.passengerWeights.clone();
        this.type = transport.type;
        this.maxSpeed = transport.maxSpeed;
        transportCount++;
    }

    public static Transport promptCreate()
    {
        Scanner scan = new Scanner(System.in);
        
        // Number of passengers
        System.out.print("Enter numPassengers: ");
        int numPassengers = scan.nextInt();
        
        // Weight for each passenger
        int[] passengerWeights = new int[numPassengers];
        for (int i = 0; i < numPassengers; i++)
        {
            System.out.format("Enter passengerWeights[%d] (in kg): ", i);
            passengerWeights[i] = scan.nextInt();
        }
        
        // The type
        System.out.print("Enter type (");
        Transport.Type[] allTypes = Transport.Type.values();
        for (int i = 0; i < allTypes.length; i++)
        {
            System.out.print(allTypes[i]);
            if (i != allTypes.length - 1)
            {
                System.out.print(", ");
            }
        }
        System.out.print("): ");
        scan.nextLine(); // skip the previous enter
        String typeString = scan.nextLine().toUpperCase();
        Transport.Type type = Transport.Type.valueOf(typeString);

        // Max speed
        System.out.print("Enter max speed (in km/hr): ");
        int maxSpeed = scan.nextInt();

        scan.close();

        return new Transport(numPassengers, passengerWeights, type, maxSpeed);
    }

    public static Transport createRandom()
    {
        Random rand = new Random();
        int numPassengers = rand.nextInt(11);
        int[] passengerWeights = new int[numPassengers];
        for (int i = 0; i < numPassengers; i++)
        {
            passengerWeights[i] = 20 + rand.nextInt(101);
        }
        Transport.Type[] allTypes = Transport.Type.values();
        Transport.Type type = allTypes[rand.nextInt(allTypes.length)];
        int maxSpeed = 10 + rand.nextInt(1000);

        return new Transport(numPassengers, passengerWeights, type, maxSpeed);
    }

    public int getTotalPassengerWeight()
    {
        int sum = 0;
        for (int i : passengerWeights)
        {
            sum += i;
        }
        return sum;
    }   

    public boolean compareByNumPassengersTo(Transport otherTransport)
    {
        return numPassengers == otherTransport.numPassengers;
    }

    public static boolean compareByTotalTotalPassengerWeight(Transport a, Transport b)
    {
        return a.getTotalPassengerWeight() == b.getTotalPassengerWeight();
    }

    public void print()
    {
        System.out.printf("Number of passengers: %d\n", numPassengers);
        System.out.print("Passenger weights: ");
        for (int i = 0; i < numPassengers; i++)
        {
            System.out.print(passengerWeights[i]);
            if (i != numPassengers - 1)
            {
                System.out.print(", ");
            }
        }
        System.out.printf("\nThe type is: %s\n", this.type.toString());
        System.out.printf("The max speed is: %d\n", maxSpeed);
    }

    // PENTRU NOTA 10.

    // As putea utiliza (de)serializarea, pentru a face acest lucru automat, dar presupun ca vreti 
    // sa facem acest lucru manual. Insa serializarea automata este destul de usoara in java.
    // https://www.vogella.com/tutorials/JavaSerialization/article.html

    // Populates the object with fields from the given file.
    public Transport(String fileName)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(fileName);
            Scanner scan = new Scanner(inputStream);
            this.numPassengers = scan.nextInt();
            this.passengerWeights = new int[numPassengers];
            for (int i = 0; i < numPassengers; i++)
            {
                passengerWeights[i] = scan.nextInt();
            }
            scan.nextLine(); // Skip the new line character. 
            this.type = Transport.Type.valueOf(scan.nextLine());
            this.maxSpeed = scan.nextInt();
            scan.close();
        }
        catch (FileNotFoundException exception)
        {
            System.err.printf("File %s not found.", fileName);
        }
        catch (InputMismatchException exception)
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
            Writer writer = new FileWriter(fileName);
            writer.append(String.valueOf(numPassengers));
            writer.append('\n');
            for (Integer i : passengerWeights)
            {
                writer.append(i.toString());
                writer.append('\n');
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