# Tehnici avansate de programare (Java), Laborator nr.4

Выполнил: **Curmanschii Anton**, IA1901.

[Ссылка на весь код для этой лабы на гитхабе](https://github.com/AntonC9018/uni_java/tree/master/lab4).

## Задания

1. Определить новый класс пассажира с некими полями (я выбрал вес, рост и имя), с методами гет и сет, конструктором с параметрами, конструктором без параметров для случайной инициализации, и методами отображения объекта в консоли. Я дополнительно добавил метод сериализации и десериализации.
2. На базе лабораторной работы номер 1, сменить массив с массой пассажиров на `ArrayList` пассажиров.
3. Исправить методы, которые работали с массивом, чтобы они работали с `ArrayList`.
4. Исправить `main()`.
5. Использовать циклы `for` и `foreach` для итерации по `ArrayList`.
6. Показать в *Intellij Idea*.

## Реализация

### Passenger

Определим класс `Passenger` с 3 приватными полями: вес, рост и имя. Добавлю и методы гет, сет для всех полей. Сделал геттеры и сеттеры *без* проверки значений (рост и вес должны быть больше 0).

```java
public class Passenger 
{
    private String name;
    private int weight;
    private int height;

    // get, set ...
}
```

Конструктор-рандомизатор достает имена из статического массива имен, а числа генерирует из стаического генератора чисел.

```java
private static final String[] namesPool = {
    "John", "Ben", "Jim", "Kate"
};

private static final Random random = new Random();

public Passenger()
{
    this.name = namesPool[random.nextInt(namesPool.length)];
    this.weight = random.nextInt(100) + 5;
    this.height = random.nextInt(100) + 5;
}
```

Конструктор с параметрами использует мой вспомогательный класс для инпута. Новые методы этого класса будут упомянуты позже.

```java
public static Passenger promptInput()
{
    String name = Prompt.string("name", 3);
    int weight = Prompt.int_("weight", 5, 200);
    int height = Prompt.int_("height", 5, 200);

    return new Passenger(name, weight, height);
}
```

Методы отображения, сериализации и десериализации. Решил вынести разрешение ошибки во внешний класс, поскольку он ответственнен за формат файла.

```java
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
```

### ArrayList

Сменим массив на `ArrayList`. `ArrayList` принимает один генерик аргумент: тип элементов.

```java
private ArrayList<Passenger> passengers;
```

Для заполнения массива элементами, используем метод `add()`, а для удаления элементов — метод `remove()`, а для получения числа элементов листа — метод `size()`. Вот, к примеру, метод сет числа пассажирова:

```java
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
```

Я метод `get()` позволяет получить элемент по индексу. К примеру:

```java
ArrayList<Passenger> ghostWeights = ghost.getPassengers();
ghostWeights.get(0).setWeight(50);
ghostWeights.get(1).setWeight(2);
```

Метод `set()` позволяет выставить элемент по индексу. Я знаю про этот метод, но не было необходимости его использовать.

Метод суммирования веса пассажиров использует способ итерации `foreach` по листу: 
```java
public int getTotalPassengerWeight()
{
    int sum = 0;
    for (Passenger p : passengers)
    {
        sum += p.getWeight();
    }
    return sum;
}  
```

Еще один способ итерации по всему массиву — метод `forEach()` + лямбда. Например:

```java
passengers.forEach(p -> p.print());

// эквивалентно этому

for (int i = 0; i < passenger.size(); i++)
{
    passengers.get(i).print();
}
```

## Новые методы в Prompt

### `Prompt.string()`

Возвращает строку, полученную с консоли. Минимальная длина принимается параметром. Пример вызова уже видели раньше.

```java
public static String string(String message, int minLength)
{
    while (true)
    {
        System.out.printf("Enter %s (at least %d characters): ", message, minLength);
        String input = scanner.nextLine();
        
        if (input.length() >= minLength)
            return input;
        
        System.out.printf("The %s is too short (%d characters long), must be at least %d characters long.\n", message, input.length(), minLength);
    }
}
```

### `Prompt._enum()`

Считывает член заданного енума, по названию, и возвращает его экземпляр. Этот генерик метод, который принимает информацию о классе енума, и использует этот объект для конвертирования строки в элемент енума, а также выведение на экран всех возможных членов енума.

```java
public static <T extends Enum<T>> T _enum(String message, Class<T> enumClass)
{
    T type = null;
    T[] allTypes = enumClass.getEnumConstants();

    do
    {
        System.out.printf("Enter %s (", message);
        for (int i = 0; i < allTypes.length; i++)
        {
            System.out.print(allTypes[i]);
            if (i != allTypes.length - 1)
            {
                System.out.print(", ");
            }
        }
        System.out.print("): ");
        String typeString = scanner.nextLine().toUpperCase();
        try
        {
            type = Enum.valueOf(enumClass, typeString);
            break;
        }
        catch (IllegalArgumentException exception)
        {
            System.out.printf("\"%s\" in not a valid %s.\n", typeString, message);
            continue;
        }
    } while (type == null);

    return type;
}
```

Пример использования в классе `Transport` в методе инициализации с консоли:

```java
Transport.Type type = Prompt._enum("type", Transport.Type.class);
```

## Запуск кода

Я не менял код мейна: только исправил, чтобы он работал корректно с листом.

```
====== Train ======
Number of passengers: 4
Passengers:
Name: Steve, Weight: 10, Height: 10.
Name: Josh, Weight: 10, Height: 60.
Name: Jaiden, Weight: 30, Height: 20.
Name: Karl, Weight: 50, Height: 10.
The type is: TRAIN
The max speed is: 200
====== Ghost ======
Number of passengers: 0
Passengers:
The type is: NOT_SPECIFIED
The max speed is: 0
=== Train Copy ====
Number of passengers: 4
Passengers:
Name: Steve, Weight: 10, Height: 10.
Name: Josh, Weight: 10, Height: 60.
Name: Jaiden, Weight: 30, Height: 20.
Name: Karl, Weight: 50, Height: 10.
The type is: TRAIN
The max speed is: 200
==== Empty car ====
Number of passengers: 0
Passengers:
The type is: CAR
The max speed is: 150
===== Random ======
Number of passengers: 6
Passengers:
Name: Ben, Weight: 20, Height: 101.
Name: Jim, Weight: 50, Height: 13.
Name: Jim, Weight: 81, Height: 11.
Name: Kate, Weight: 78, Height: 89.
Name: Jim, Weight: 75, Height: 32.
Name: Kate, Weight: 55, Height: 98.
The type is: TRAIN
The max speed is: 662
===================
... Expecting random values for the weights ...
Number of passengers: 5
Passengers:
Name: Kate, Weight: 31, Height: 52.
Name: Ben, Weight: 21, Height: 66.
Name: Jim, Weight: 48, Height: 91.
Name: Kate, Weight: 100, Height: 27.
Name: John, Weight: 83, Height: 61.
The type is: NOT_SPECIFIED
The max speed is: 0
===================
Number of passengers: 5
Passengers:
Name: Kate, Weight: 50, Height: 52.
Name: Ben, Weight: 2, Height: 66.
Name: Jim, Weight: 48, Height: 91.
Name: Kate, Weight: 100, Height: 27.
Name: John, Weight: 83, Height: 61.
The type is: NOT_SPECIFIED
The max speed is: 0
===================
Number of passengers: 1
Passengers:
Name: Mark, Weight: 1, Height: 1.
The type is: NOT_SPECIFIED
The max speed is: 0
===================
Train has a different number of passengers than the empty car.
Train has the same number of passengers as the empty car.
===================
Train has a different passenger weight (100) than the random (359).
Train has the same passenger weight (100) as the train copy.
===================
Average passenger weight of the random is 59.833332.
===================
Numarul total de transporturi create: 5
===================
Saving the train to disk as train.txt.
Reading the train back in from disk.
Printing out the deserialized train.
Number of passengers: 4
Passengers:
Name: Steve, Weight: 10, Height: 10.
Name: Josh, Weight: 10, Height: 60.
Name: Jaiden, Weight: 30, Height: 20.
Name: Karl, Weight: 50, Height: 10.
The type is: TRAIN
The max speed is: 200
===================
Enter number of passengers: 1
Enter name (at least 3 characters): 1
The name is too short (1 characters long), must be at least 3 characters long.
Enter name (at least 3 characters): 1
The name is too short (1 characters long), must be at least 3 characters long.
Enter name (at least 3 characters): 1
The name is too short (1 characters long), must be at least 3 characters long.
Enter name (at least 3 characters): 1234
Enter weight (5 <= x <= 200): 5
Enter height (5 <= x <= 200): 6
Enter type (NOT_SPECIFIED, AIRPLANE, CAR, BICYCLE, TRAIN): train
Enter max speed: 1
Number of passengers: 1
Passengers:
Name: 1234, Weight: 5, Height: 6.
The type is: TRAIN
The max speed is: 1
```

Сериализованный поезд:
```
4
Steve
10
10
Josh
10
60
Jaiden
30
20
Karl
50
10
TRAIN
200
```
