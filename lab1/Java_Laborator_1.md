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
public class Main {
    public static void main(String[] args)
    {
        int[] weights = { 10, 10, 30, 50 };
        Transport train     = new Transport(weights, Transport.Type.TRAIN, 200);
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
            // The rest of the array will be filled with 0's.
            ghost.setNumPassengers(5);
            
            System.out.println("===================");
            System.out.println("... Expecting zeros for the weights ...");
            ghost.print();
            
            // Setting the weights using a reference to the internal array.
            int[] ghostWeights = ghost.getPassengerWeights();
            ghostWeights[0] = 50;
            ghostWeights[1] = 2;

            System.out.println("===================");
            ghost.print();

            // Setting a new array of weights.
            ghost.setPassengerWeights(new int[] { 1, 2, 3, 4 });

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
```

Решил удалить поле numPassengers, но оставил его как свойство (гет сет методы). Мотивируется это решение тем, что данная информация уже доступна через passengerWeights.length.

```java
// transport.java
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

    private int[] passengerWeights;
    public enum Type {
        NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN
    };
    private Type type;
    private int maxSpeed;

    public int getNumPassengers() 
    {
        return passengerWeights.length;
    }

    // Schimbam numarul de elemente in passengerWeights
    public void setNumPassengers(int numPassengers) 
    {
        int[] prevWeights = passengerWeights;
        passengerWeights = new int[numPassengers];

        for (int i = 0; i < Math.min(prevWeights.length, numPassengers); i++)
        {
            passengerWeights[i] = prevWeights[i];
        }
    }

    public int[] getPassengerWeights() 
    {
        return passengerWeights;
    }

    public void setPassengerWeights(int[] passengerWeights) 
    {
        this.passengerWeights = passengerWeights;
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
        this.passengerWeights = new int[0];
        this.type = Type.NOT_SPECIFIED;
        transportCount++;
    }

    public Transport(int[] passengerWeights, Transport.Type type, int maxSpeed) {
        this.passengerWeights = passengerWeights;
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport.Type type, int maxSpeed)
    {
        this.passengerWeights = new int[0];
        this.type = type;
        this.maxSpeed = maxSpeed;
        transportCount++;
    }

    public Transport(Transport transport)
    {
        this.passengerWeights = transport.passengerWeights.clone();
        this.type = transport.type;
        this.maxSpeed = transport.maxSpeed;
        transportCount++;
    }

    public static Transport promptCreate()
    {
        Scanner scan = new Scanner(System.in);
        
        // This, however, only handles NEGATIVE VALUES.
        int numPassengers;
        do
        {
            // Number of passengers
            System.out.print("Enter numPassengers: ");
            
            // The scan.nextInt() can also THROW if it doesn't get string that can be 
            // converted in a number. If that happens, the program just crashes.
            numPassengers = scan.nextInt();

            // Here's the implementation with exception handling
            // However, doing this for every variable is a lot of boilerplate.
            // In order to avoid this, a new class for input has to be defined, which would
            // get rid of the code repetition, however, this is well beyond the scope of this lab.
            /*
            try
            {
                numPassengers = scan.nextInt();
            }
            catch (InputMismatchException exception)
            {
                numPassengers = 0;
                continue;
            }
            */

        } while (numPassengers < 0);
        
        // Weight for each passenger
        int[] passengerWeights = new int[numPassengers];
        for (int i = 0; i < numPassengers; i++)
        {
            do
            {
                System.out.format("Enter passengerWeights[%d] (in kg): ", i);
                passengerWeights[i] = scan.nextInt();
            } while (passengerWeights[i] < 0);
        }
        scan.nextLine(); // skip the previous enter
        
        // The type
        Transport.Type type = null;
        do
        {
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
            String typeString = scan.nextLine().toUpperCase();
            try
            {
                type = Transport.Type.valueOf(typeString);
                break;
            }
            catch (IllegalArgumentException exception)
            {
                continue;
            }
        } while (type == null);

        // Max speed
        int maxSpeed;
        do
        {
            System.out.print("Enter max speed (in km/hr): ");
            maxSpeed = scan.nextInt();
        } while (maxSpeed < 0);

        scan.close();

        return new Transport(passengerWeights, type, maxSpeed);
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

        return new Transport(passengerWeights, type, maxSpeed);
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
        return getNumPassengers() == otherTransport.getNumPassengers();
    }

    public static boolean compareByTotalTotalPassengerWeight(Transport a, Transport b)
    {
        return a.getTotalPassengerWeight() == b.getTotalPassengerWeight();
    }

    public void print()
    {
        System.out.printf("Number of passengers: %d\n", getNumPassengers());
        System.out.print("Passenger weights: ");
        for (int i = 0; i < passengerWeights.length; i++)
        {
            System.out.print(passengerWeights[i]);
            if (i != passengerWeights.length - 1)
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
            int numPassengers = scan.nextInt();
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
            writer.append(String.valueOf(passengerWeights.length));
            writer.append('\n');
            for (int i : passengerWeights)
            {
                writer.append(String.valueOf(i));
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

Compilez programul cu `javac main.java`. 

Pornesc programul cu `java Main`.

```
====== Train ======
Number of passengers: 4
Passenger weights: 10, 10, 30, 50
The type is: TRAIN
The max speed is: 200
====== Ghost ======
Number of passengers: 0
Passenger weights:
The type is: NOT_SPECIFIED
The max speed is: 0
=== Train Copy ====
Number of passengers: 4
Passenger weights: 10, 10, 30, 50
The type is: TRAIN
The max speed is: 200
==== Empty car ====
Number of passengers: 0
Passenger weights:
The type is: CAR
The max speed is: 150
===== Random ======
Number of passengers: 10
Passenger weights: 61, 88, 71, 71, 32, 106, 75, 27, 73, 115
The type is: NOT_SPECIFIED
The max speed is: 994
===================
... Expecting zeros for the weights ...
Number of passengers: 5
Passenger weights: 0, 0, 0, 0, 0
The type is: NOT_SPECIFIED
The max speed is: 0
===================
Number of passengers: 5
Passenger weights: 50, 2, 0, 0, 0
The type is: NOT_SPECIFIED
The max speed is: 0
===================
Number of passengers: 4
Passenger weights: 1, 2, 3, 4
The type is: NOT_SPECIFIED
The max speed is: 0
===================
Train has a different number of passengers than the empty car.
Train has the same number of passengers as the empty car.
===================
Train has a different passenger weight (100) than the random (719).
Train has the same passenger weight (100) as the train copy.
===================
Average passenger weight of the random is 71.900002.
===================
Numarul total de transporturi create: 5
===================
Saving the train to disk as train.txt.
Reading the train back in from disk.
Printing out the deserialized train.
Number of passengers: 4
Passenger weights: 10, 10, 30, 50
The type is: TRAIN
The max speed is: 200
===================  
Enter numPassengers: -2
Enter numPassengers: 3
Enter passengerWeights[0] (in kg): -5
Enter passengerWeights[0] (in kg): 9
Enter passengerWeights[1] (in kg): 15
Enter passengerWeights[2] (in kg): 20
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): l
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): jksfdjksfd
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): train
Enter max speed (in km/hr): 5
Number of passengers: 3
Passenger weights: 9, 15, 20
The type is: TRAIN
The max speed is: 5                                                   
```

Заостряю внимание на моменте с валидацией ввода. Отрицательное число пассажиров, отрицательный вес пассажиров и отрицательная максимальная скорость не допускаются. В случае их воода, данные просто запрашиваются еще раз. 

В случае, если вводится строка, которая не конвиртируется в число, программа крашится. Однако, в коде отмечено комментарием, как это исправить.

Невалидные значения енума тоже не допускаются.

```
Enter numPassengers: -2
Enter numPassengers: 3
Enter passengerWeights[0] (in kg): -5
Enter passengerWeights[0] (in kg): 9
Enter passengerWeights[1] (in kg): 15
Enter passengerWeights[2] (in kg): 20
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): l
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): jksfdjksfd
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): train
```

## Concluziile

In general, am avut experienta cu java cu 3 ani in urma, cand incepeam sa studiez programarea. Am facut ceva asemanator si la inceputul anului acesta, la C++, deci nu este ceva nou. 

Pentru mine, faptul ca C are pointeri nu ma deranjeaza - chiar inversul. Eu sunt tipul de programator care apreciaza flexibilitatea ce aduce nivelul scazut al lui C. 

Eu personal nu am aflat in acest laborator nimic nou, deoarece deja m-am dus prin aceasta: clasele, modificatori de acces, proprietati, memrii statici, constructori, metode membre si cele statice, lucru cu input stream si output stream, fisierele, formatarea, structuri de date (array list, linked list, has map) etc.