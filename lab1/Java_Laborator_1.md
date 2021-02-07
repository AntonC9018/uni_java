# Tehnici avansate de programare (Java), Laborator nr.1

A efectuat: **Curmanschii Anton**, IA1901.

## Sarcina (Varianta 9)

De creat clasa `Transport` cu câteva câmpuri. 
	
Câmpurile obligatorii:
- un cîmp de tip intreg (int), pentru păstrarea numărul de pasageri ai transportului;
- un vector dinamic, în care se păstrează informaţia despre greutatea fiecatui pasager;
- un câmp de tip intreg, static, se păstrează numărul de obiecte create ale clasei;

Câmpuri opţionale: tipul (avion, autocar etc), marca, viteza, virsta =perioda de funţionare a transportului. 

De creat trei tipuri  de constructori pentru această clasă (cu alocare dinamică a memoriei):
- constructor standard (fără parametri) 
- câteva constructori cu parametri
- constructor de copiere

- De creat metodele pentru acces la toate câmpurile clasei şi de modificare a lor (metodele set() si get()).
- De creat metoda pentru a afişa la ecran toată informaţia despre `Transport`, adică toate câmpurile din această clasă.
- De descris metoda, care completeaza toate campurile obiectului cu valori citite de la tastatura.
- De descris metoda, care completeaza toate campurile obiectului cu valori aleatoare.
- De creat metoda, care calculează greutate totală a pasagerilor unui `Transport`. 
- De creat metoda, ce compară două `Transport`uri (două instanţe (obiecte) a clasei : obiectului curent cu obiectul primit ca parametru) în baza numărului de pasageri.
- De creat metoda statică, ce compară doi transporturi (două instanţe (obiecte) a clasei – ambele obiecte primim ca parametrii) după greutatea sumară a tuturor pasagerilor.

În metoda main() de creat cîteva `Transport`uri, utilizând toţi constructorii declaraţi. După creerea fiecărei instantze a clasei `Transport`, de afişat la ecran câmpurile cu ajutorul metodei respective. De creat un vector dinamic din obiectele clasei şi de initializat vectorul creat, folosind 3 tipuri de constructor. De afişat la ecran (in ciclu) informatia a tuturor transporturi în vector.  De comparat câteva perechi de `Transport`uri  în baza numărului de pasageri şi câteva perechi -  în baza greutătii sumare a pasagerilor cu ajutorul metodei respective.  Rezultatele comparării de afişat la ecran. De calculat greutatea medie a pasagerilor unui `Transport`. Rezultatele de afişat la ecran.
De afişat la ecran numarul general de `Transport`uri creaţi, folosind câmpul static clasei.

Pentru nota 10. Adaugator pentru tot ce este descris mai sus:
- un constructor care primeşte ca parametru denumirea fişierului textual (String), de unde se incarcă valori pentru câmpurile obiectului creat. 
- o funcţie care înscrie toate cîmpurile clasei în fişier, numele fisierului se indică ca parametru la intrare.
- În funcţia main pentru toate obiectele create de salvat  datele în fişiere, denumirile cărora se preiau de la numele obiectelor. 

## Codul

```java
// main.java
import java.util.ArrayList;

public class Main {
    public static void main(String[] args)
    {
        ArrayList<Integer> weights = new ArrayList<Integer>(4);
        weights.add(50);
        weights.add(50);
        weights.add(50);
        weights.add(50);
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
```

```java
// transport.java
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Transport {
    private static int transportCount;

    private int numPassengers;
    private ArrayList<Integer> passengerWeights;
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

    public ArrayList<Integer> getPassengerWeights() 
    {
        return passengerWeights;
    }

    public void setPassengerWeights(ArrayList<Integer> passengerWeights) 
    {
        this.passengerWeights = passengerWeights;
    }

    public void setNumPassengers(int numPassengers) 
    {
        this.numPassengers = numPassengers;
    }

    public Transport()
    {
        this.passengerWeights = new ArrayList<Integer>();
        this.type = Type.NOT_SPECIFIED;
        transportCount++;
    }

    public Transport(int numPassengers, ArrayList<Integer> passengerWeights, Transport.Type type, int maxSpeed) {
        assert(passengerWeights.size() == numPassengers);
        this.numPassengers = numPassengers;
        this.passengerWeights = passengerWeights;
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport.Type type, int maxSpeed)
    {
        this.numPassengers = 0;
        this.passengerWeights = new ArrayList<Integer>();
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport transport)
    {
        this.numPassengers = transport.numPassengers;
        this.passengerWeights = new ArrayList<>(transport.passengerWeights);
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
        ArrayList<Integer> passengerWeights = new ArrayList<Integer>(numPassengers);
        for (int i = 0; i < numPassengers; i++)
        {
            System.out.format("Enter passengerWeights[%d] (in kg): ", i);
            passengerWeights.add(scan.nextInt());
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
        ArrayList<Integer> passengerWeights = new ArrayList<Integer>(numPassengers);
        for (int i = 0; i < numPassengers; i++)
        {
            passengerWeights.add(20 + rand.nextInt(101));
        }
        Transport.Type[] allTypes = Transport.Type.values();
        Transport.Type type = allTypes[rand.nextInt(allTypes.length)];
        int maxSpeed = 10 + rand.nextInt(1000);

        return new Transport(numPassengers, passengerWeights, type, maxSpeed);
    }

    public int getTotalPassengerWeight()
    {
        int sum = 0;
        for (Integer i : passengerWeights)
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
            System.out.print(passengerWeights.get(i));
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
            this.passengerWeights = new ArrayList<Integer>(numPassengers);
            for (int i = 0; i < numPassengers; i++)
            {
                passengerWeights.add(scan.nextInt());
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
```

## Rezultatele

Compilez programul cu `javac main.java transport.java`. 

Pornesc programul cu `java Main`.

```
====== Train ======                                                       
Number of passengers: 4                                                   
Passenger weights: 50, 50, 50, 50                                         
The type is: TRAIN                                                        
The max speed is: 200                                                     
====== Ghost ======                                                       
Number of passengers: 0                                                   
Passenger weights:                                                        
The type is: NOT_SPECIFIED                                                
The max speed is: 0                                                       
=== Train Copy ====                                                       
Number of passengers: 4                                                   
Passenger weights: 50, 50, 50, 50                                         
The type is: TRAIN                                                        
The max speed is: 200                                                     
==== Empty car ====                                                       
Number of passengers: 0                                                   
Passenger weights:                                                        
The type is: CAR                                                          
The max speed is: 150                                                     
===== Random ======                                                       
Number of passengers: 10                                                  
Passenger weights: 104, 108, 77, 114, 96, 107, 95, 65, 110, 22            
The type is: CAR                                                
The max speed is: 654                                                     
===================                                                       
Train has a different number of passengers than the empty car.            
Train has the same number of passengers as the empty car.                 
===================                                                       
Train has a different passenger weight (200) than the random (898).       
Train has the same passenger weight (200) as the train copy.              
===================                                                       
Average passenger weight of the random is 89.800003.                      
===================                                                       
Numarul total de transporturi create: 5                                   
===================                                                       
Saving the train to disk as train.txt.                                    
Reading the train back in from disk.                                      
Printing out the deserialized train.                                      
Number of passengers: 4                                                   
Passenger weights: 50, 50, 50, 50                                         
The type is: TRAIN                                                        
The max speed is: 200                                                     
===================                                                       
Enter numPassengers: 4                                                    
Enter passengerWeights[0] (in kg): 1                                      
Enter passengerWeights[1] (in kg): 2                                      
Enter passengerWeights[2] (in kg): 3                                      
Enter passengerWeights[3] (in kg): 4                                      
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): bicycle        
Enter max speed (in km/hr): 89                                            
Number of passengers: 4                                                   
Passenger weights: 1, 2, 3, 4                                             
The type is: BICYCLE                                                      
The max speed is: 89                                                      
```

## Concluziile

In general, am avut experienta cu java cu 3 ani in urma, cand incepeam sa studiez programarea. Am facut ceva asemanator si la inceputul anului acesta, la C++, deci nu este ceva nou. 

Pentru mine, faptul ca C are pointeri nu ma deranjeaza - chiar inversul. Eu sunt tipul de programator care apreciaza flexibilitatea ce aduce nivelul scazut al lui C. 

Eu personal nu am aflat in acest laborator nimic nou, deoarece deja m-am dus prin aceasta: clasele, modificatori de acces, proprietati, memrii statici, constructori, metode membre si cele statice, lucru cu input stream si output stream, fisierele, formatarea, structuri de date (array list, linked list, has map) etc.

In opinia mea codul este auto-explicativ, dar daca trebuie sa explic unele momente din cod — nu am probeleme cu aceasta. Voi fi la universitate saptamana viitoare (probabil).