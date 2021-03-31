import java.util.ArrayList;

public class Main {
    public static void main(String[] args)
    {
        ArrayList<Passenger> passengers = new ArrayList<Passenger>();
        passengers.add(new Passenger("Steve", 10, 10));
        passengers.add(new Passenger("Josh", 10, 60));
        passengers.add(new Passenger("Jaiden", 30, 20));
        passengers.add(new Passenger("Karl", 50, 10));

        Transport train     = new Transport(passengers, Transport.Type.TRAIN, 200);
        Transport ghost     = new Transport();
        Transport trainCopy = new Transport(train);
        Transport emptyCar  = new Transport(Transport.Type.CAR, 150);
        Transport random    = Transport.createRandom();

        {
            Transport[] transports = { train, ghost, trainCopy, emptyCar, random };
            String[] transportTitles = {
                "====== Train ======",
                "====== Ghost ======",
                "=== Train Copy ====",
                "==== Empty car ====",
                "===== Random ======"
            };
            for (int i = 0; i < transports.length; i++)
            {
                System.out.println(transportTitles[i]);
                transports[i].print();
            }
        }
        {
            // The rest of the array will be filled with random passengers.
            ghost.setNumPassengers(5);
            
            System.out.println("===================");
            System.out.println("... Expecting random values for the weights ...");
            ghost.print();
            
            // Setting the weights using a reference to the internal array.
            ArrayList<Passenger> ghostWeights = ghost.getPassengers();
            ghostWeights.get(0).setWeight(50);
            ghostWeights.get(1).setWeight(2);

            System.out.println("===================");
            ghost.print();
            

            ArrayList<Passenger> newPassengers = new ArrayList<Passenger>();
            newPassengers.add(new Passenger("Mark", 1, 1));

            // Setting a new array of weights.
            ghost.setPassengers(newPassengers);

            System.out.println("===================");
            ghost.print();
            System.out.println("===================");
        }
        {
            // The train has more passengers, so the first one will be printed.
            if (train.compareByNumPassengersTo(emptyCar))
            {
                System.out.println("Train has the same number of passengers as the empty car.");
            }
            else
            {
                System.out.println("Train has a different number of passengers than the empty car.");
            }

            emptyCar.setNumPassengers(train.getNumPassengers());
            
            // The train has the same amount of passengers, since we've just adjusted the car.
            if (train.compareByNumPassengersTo(emptyCar))
            {
                System.out.println("Train has the same number of passengers as the empty car.");
            }
            else
            {
                System.out.println("Train has a different number of passengers than the empty car.");
            }
            System.out.println("===================");
        }
        {
            // These will be most likely be different
            if (Transport.compareByTotalTotalPassengerWeight(train, random))
            {
                System.out.printf("Train has the same passenger weight (%d) as the random.\n", 
                    train.getTotalPassengerWeight());
            }
            else
            {
                System.out.printf("Train has a different passenger weight (%d) than the random (%d).\n", 
                    train.getTotalPassengerWeight(), random.getTotalPassengerWeight());
            }
            
            // These will be the same
            if (Transport.compareByTotalTotalPassengerWeight(train, trainCopy))
            {
                System.out.printf("Train has the same passenger weight (%d) as the train copy.\n", 
                    train.getTotalPassengerWeight());
            }
            else
            {
                System.out.printf("Train has a different passenger weight (%d) than the train copy (%d).\n", 
                    train.getTotalPassengerWeight(), trainCopy.getTotalPassengerWeight());
            }
            System.out.println("===================");
        }
        {
            System.out.printf("Average passenger weight of the random is %f.\n", 
                (float)random.getTotalPassengerWeight() / (float)random.getNumPassengers());
            System.out.println("===================");
        }
        {
            System.out.printf("Numarul total de transporturi create: %d\n", Transport.getTransportCount());
            System.out.println("===================");
        }
        {
            String fileName = "train.txt";
            System.out.printf("Saving the train to disk as %s.\n", fileName);
            train.serialize(fileName);

            System.out.println("Reading the train back in from disk.");
            Transport deserializedTrain = new Transport(fileName);
            System.out.println("Printing out the deserialized train.");
            deserializedTrain.print();
            System.out.println("===================");
        }
        {
            // Citirea de la tastatura.
            Transport result = Transport.promptCreate();
            result.print();
        }
    }
}
