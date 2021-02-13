public class Main {
    public static void main(String[] args)
    {
        int[] weights = new int[] { 10, 10, 30, 50 };
        Transport train = new Transport(4, weights, Transport.Type.TRAIN, 200);
        Transport ghost = new Transport();
        Transport trainCopy = new Transport(train);
        Transport emptyCar = new Transport(Transport.Type.CAR, 150);
        Transport random = Transport.createRandom();

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
            System.out.println("===================");
        }
        {
            // The train has more passengers, so the first one will be printed
            if (train.compareByNumPassengersTo(emptyCar))
            {
                System.out.println("Train has the same number of passengers as the empty car.");
            }
            else
            {
                System.out.println("Train has a different number of passengers than the empty car.");
            }
            // Set the number of passengers in the empty car equal to the number of passengers in the train. 
            // Note that this would invalidate the integrity of the empty car, since the passenger weight 
            // vector is not adjusted accordingly, so realistically this change should not be even allowed.
            // The setter ideally should take an array of weights to use to adjust the vector.
            // Come to think of it, the vector setter should also adjust the number of passengers accordingly,
            // or this action shouldn't even be allowed.
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
