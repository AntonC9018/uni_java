# Tehnici avansate de programare (Java), Laborator nr.5

Тема: **Многопоточность**.

Выполнил: **Curmanschii Anton**, IA1901.

[Ссылка на весь код для этой лабы на гитхабе](https://github.com/AntonC9018/uni_java/tree/master/lab5).

## Задание (Вариант 9)

Se dă matrice de tip char. De divizat matricea pe coloane, fiecare coloană fiind atribuită unui fir de execuție separat. Fiecare fir trebuie să găsească numărul de operații aritmetice (‘+’, ‘-‘, ‘*’, ‘/’) în coloana prelucrată. În main de afișat raportul dintre numărul total de operații aritmetice din matrice cu numărul total de elemente din matrice. De utilizat interfața Runnable.

# Реализация

Реализовал 2 варианта: со сбором результатов вручную в мейне, и со сложением полученных непосредственно в методах `run()` классов, подсчитывающих количество операций, в блоке `synchronized`.

## Сбор вручную

Определим следующий класс, который просто будет контейнером для текущей суммы. В него, помимо вышеупомянутых операций, добавил еще и `%`, аля мод (когда понял что не надо, было уже поздно).

```java
class Counts
{
    public int div;
    public int mul;
    public int mod;
    public int plus;
    public int minus;

    public void add(Counts other)
    {
        div += other.div;
        mul += other.mul;
        mod += other.mod;
        plus += other.plus;
        minus += other.minus;
    }

    public int countAll()
    {
        return div + mul + mod + plus + minus;
    }
}
```

С этим классом будет работать класс для подсчета, `CharCounter`, который имплементит интерфейс `Runnable`. В своей функции `run()` он будет считать, сколько раз он встретил определенный символ операции в данном ему ряду:

```java
public class CharCounter implements Runnable 
{
    private char[] row;
    private Counts counts;

    public CharCounter(char[] row)
    {
        this.row = row;
        counts = new Counts();
    }

    public void run() 
    {
        for (char ch : row)
        {
            switch (ch)
            {
            case '+':
                counts.plus++;
                break;
            case '-':
                counts.minus++;
                break;
            case '*':
                counts.mul++;
                break;
            case '/':
                counts.div++;
                break;
            case '%':
                counts.mod++;
                break;
            }
        }
    }

    public Counts getResult()
    {
        return counts;
    }
}
```

В мейне, определим таблицу, создадим массивы под счетчики и под собственно треды и запустим их на нашей матрице.
```java
char matrix[][] = {
    {'-', '+', '/', '*', '%', '1', '1' },
    {'+', '+', '+', '+', '+', '+', '+' },
    {'-', '+', '/', '^', '%', '1', '/' },
    {'-', '+', 'h', '*', '%', '1', '1' },
    {'+', '+', '/', '+', '%', '+', '1' },
    {'-', '+', '+', '+', '%', '%', '%' }
};
CharCounter counters[] = new CharCounter[matrix.length];
Thread threads[] = new Thread[matrix.length];

for (int i = 0; i < threads.length; i++)
{
    counters[i] = new CharCounter(matrix[i]);
    threads[i] = new Thread(counters[i]);
    threads[i].start();
}
```

Найдем общее количество элементов в таблице:
```java
int totalCharacters = 0;
for (int i = 0; i < matrix.length; i++)
{
    totalCharacters += matrix[i].length;
}
```

Далее, в цикле будем ожидать завершения каждого из тредов. После завершения очередного треда, будет собирать полученный им результат в общую кучу. На этом этапе становится понятно, зачем нам хранить массив с счетчиками, а именно, чтобы собрать финальный результат методом `getResult()`. Очевидно, что сохранять сами треды нам нужно было для того, чтобы ожидать их завершения:
```java
Counts counts = new Counts();

// wait for all threads to terminate
for (int i = 0; i < threads.length; i++)
{
    try 
    {
        threads[i].join();
        counts.add(counters[i].getResult());
    } 
    catch (InterruptedException e) 
    {
        e.printStackTrace();
    }
}
```

Наконец, получим общее число операции и покажем всю статистику:
```java
int totalOperations = counts.countAll();

System.out.printf("Total number of characters: %d.\n", totalCharacters);
System.out.printf("Total number of operations: %d.\n", totalOperations);
System.out.printf("Operations percentage: %6.2f%%.\n", (float)totalOperations / totalCharacters * 100);

System.out.printf("Number of '+': %2d. Percentage of total: %6.2f%%\n", 
    counts.plus, (float)counts.plus / totalCharacters * 100);
System.out.printf("Number of '-': %2d. Percentage of total: %6.2f%%\n", 
    counts.minus, (float)counts.minus / totalCharacters * 100);
System.out.printf("Number of '/': %2d. Percentage of total: %6.2f%%\n", 
    counts.div, (float)counts.div / totalCharacters * 100);
System.out.printf("Number of '*': %2d. Percentage of total: %6.2f%%\n", 
    counts.mul, (float)counts.mul / totalCharacters * 100);
System.out.printf("Number of '%%': %2d. Percentage of total: %6.2f%%\n", 
    counts.mod, (float)counts.mod / totalCharacters * 100);
```


## Вариант с синхронизацией

Данный вариант предполагает использование общего объекта подсчета в качестве монитора. Поскольку нам больше не нужны будут ссылки на счетчики для извлечения результата, можем убрать массив с ними и упростить инициализацию тредов, однако будем передавть им общий счетчик в конструкторе (можно было сделать и статическим объектом, но таким образом, очевидно, нельзя будет считать 2 матрицы одновременно). Откомменченные строки были изменены из старого кода:

```java
// CharCounter counters[] = new CharCounter[matrix.length];
Thread threads[] = new Thread[matrix.length];
Counts counts = new Counts();

for (int i = 0; i < matrix.length; i++)
{
    // counters[i] = new CharCounter(matrix[i]);
    // threads[i] = new Thread(counters[i]);
    threads[i] = new Thread(new CharCounter2(matrix[i], counts));
    threads[i].start();
}
```

Добавим новое поле нашему счетчику, и изменим его конструктор. Я сменил и название, потому что в мейне запускаю оба примера.
```java
class CharCounter2 implements Runnable 
{
    private char[] row;
    // private Counts counts; <- remove this line, we'll be using a local
    private Counts counts_monitor; // <- this line

    public CharCounter2(char[] row, Counts counts_monitor /* <- this */)
    {
        this.row = row;
        this.counts_monitor = counts_monitor; // <- this line is also new
        // counts = new Counts(); <- remove this line too
    }
```

Сменим метод `run()`, добавив локальный объект подсчета и вышеупомянутую синхронизацию на общем объекте. Добавим у нему парочку логов, чтобы удостоверится, что замок работает. Также, удалим метод `getResult()`, потому что он нам больше не нужен:
```java
public void run() 
{
    Counts counts = new Counts();

    for (char ch : row)
    {
        switch (ch)
        {
        case '+':
            counts.plus++;
            break;
        case '-':
            counts.minus++;
            break;
        case '*':
            counts.mul++;
            break;
        case '/':
            counts.div++;
            break;
        case '%':
            counts.mod++;
            break;
        }
    }

    synchronized (counts_monitor)
    {
        System.out.println("Entered");
        counts_monitor.add(counts);
        System.out.println("Exited");
    }
}
```

Осталось убрать в мейне строчку кода со сбором результатов. Остальной код идентичен изначальному: 
```java
// wait for all threads to terminate
for (int i = 0; i < threads.length; i++)
{
    try 
    {
        threads[i].join();
        // counts.add(counters[i].getResult());    <- this one is gone
    } 
    catch (InterruptedException e) 
    {
        e.printStackTrace();
    }
}
```

То есть что мы сделали: мы дали каждому треду ссылку на объект, играющий роль замка (монитора) для куска кода, отвественный за сбор результатов. Собственно результаты собираются в этот же самый объект замка.

Вопрос: зачем было использовать локальный объект и суммировать его в общий объект-монитор, а не суммировать сразу в монитор? 
Ответ: потому что использование этого локального объекта нам и позволило параллелизовать процесс. Без этого объекта всем тредам бы пришлось ожидать своей очереди при инкрементации любого из полей этого общего объекта, что бы полность обнулило факт параллельности тредов.

## Запуск

Запустив второй вариант кода, увидим, что замок и вправду сработал: ни один тред не смог зайти в конкурентный блок пока другой из него не вышел:
```
Entered 
Exited  
Entered 
Exited  
Entered 
Exited  
Entered 
Exited  
Entered 
Exited  
Entered 
Exited  
```

Результаты суммирования у обоих примеров одинаковые:
```
Total number of characters: 42.
Total number of operations: 34.
Operations percentage:  80.95%.
Number of '+': 17. Percentage of total:  40.48%
Number of '-':  4. Percentage of total:   9.52%
Number of '/':  4. Percentage of total:   9.52%
Number of '*':  2. Percentage of total:   4.76%
Number of '%':  7. Percentage of total:  16.67%
```

## Выводы

В этой работе на простом примере показал принцип работы тредов и с тредами, а также продемонстрировал пример использования синхронизации в Java.