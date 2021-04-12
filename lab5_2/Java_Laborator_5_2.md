# Tehnici avansate de programare (Java), Laborator nr.5

Тема: **Общение между потоками**.

Выполнил: **Curmanschii Anton**, IA1901.

[Ссылка на код на гитхабе.](https://github.com/AntonC9018/uni_java/tree/master/lab5_2)

## Задача

Дана матрица размерностью $N \times N$. Эта матрица модифициуется на протяжении нескольких итераций по правилу, что к каждой клетке добавляется величина, равная сумме ее соседей. Необходимо параллелизовать процесс итераций, распределив работу на количество тредов, данное, например, с консоли от юзера.

Важно: треды должны считать и вписывать значения во второй буфер (или во временный), который в конце итерации будет сменяться с текущим. Если этого не сделать, получится, что треды переписывают данные в текущей матрице, что некорректно повлияет на вычисления.

# Реализация

Сделал 2 варианта: первый централизовано управляет потоками и итерациями, запуская на каждой итерации новое множество потоков; второй вариант использует общение между тредами через монитор для синхронизации их работы.  

## Первый вариант: централизованный координатор

Определил класс Grid, который будет хранить в себе буфер под матрицу. Добавил двойную буферизацию, то есть определил поле под еще один буфер той же размерности, что и первый, в который будут вписывать результаты вычисений треды. Можно было бы использовать и плоские массивы вместо двумерных. Это в целом не сильно бы повлияло на итоговый код.

```java
final class Grid
{
    private int[][] primaryBuffer;
    private int[][] secondaryBuffer;
    
    public Grid(int dimension)
    {
        primaryBuffer   = createBuffer(dimension);
        secondaryBuffer = createBuffer(dimension);
    }
    // ...
    public final void swapBuffers()
    {
        int[][] t = primaryBuffer;
        primaryBuffer = secondaryBuffer;
        secondaryBuffer = t;
    }
```

Сделал пару методов для суммирования и сохранения данных. Решил не делать вариант с заполнением границ нулями, поскольку проверка вышла всего одна, и она довольно понятна. 

```java
public final int at(int row, int col)
{
    if (row >= 0 && row < primaryBuffer.length 
        && col >= 0 && col < primaryBuffer[0].length)
    {
        return primaryBuffer[row][col];
    }
    return 0;
}

public final int sumAt(int row, int col)
{
    int sum = 0;
    for (int i = row - 1; i <= row + 1; i++)
    {
        for (int j = col - 1; j <= col + 1; j++)
            sum += at(i, j);
    }
    return sum;
}

public final void storeSumAt(int row, int col)
{
    secondaryBuffer[row][col] = sumAt(row, col);
}

public final void storeSumAt(int flatIndex)
{
    storeSumAt(flatIndex / dimension(), flatIndex % dimension());
}
```

Создал класс, имплементирующий `Runnable`, который будет вызывать функции суммирования на `Grid`. Это тонкая прослойка, которая просто имеет в себе начальную позицию, число элементов, доставшееся треду для обработки, и ссылку на `Grid`-родитель.

```java
class ThreadContext implements Runnable 
{
    private Grid parent;
    private int start;
    private int share;

    public ThreadContext(Grid parent, int start, int share)
    {
        this.parent = parent;
        this.start = start;
        this.share = share;
    }

    @Override
    public void run() 
    {
        for (int i = 0; i < share; i++)
        {
            parent.storeSumAt(start + i);
        }
    }
}
```

Функция для выполнения итераций подсчета делится на 2 фазы: инициализация контекстов тредов с раздачей начальных позиций и длины и выполнение заданного числа циклов.

```java
public final void performParallelIterations(int numThreads, int numIters)
{
    ThreadContext[] contexts = new ThreadContext[numThreads];
    int numElements = dimension() * dimension();
    double sharePerThread = (double)numElements / numThreads;
    double currentStart = 0;
    
    for (int i = 0; i < numThreads - 1; i++)
    {
        double nextStart = Math.ceil(sharePerThread * (i + 1));
        int start = (int)(currentStart);
        int share = (int)(nextStart - currentStart);
        currentStart = nextStart;
        contexts[i] = new ThreadContext(this, start, share);
    }
    
    // account for possible floating point errors
    {
        int lastStart = (int)currentStart;
        int lastShare = (int)numElements - lastStart;
        contexts[numThreads - 1] = new ThreadContext(this, lastStart, lastShare);
    }
        
    Thread threads[] = new Thread[numThreads];

    for (int i = 0; i < numIters; i++)
    {
        // create and run the threads
        for (int j = 0; j < numThreads; j++)
        {
            threads[j] = new Thread(contexts[j]);
            threads[j].start();
        }
        
        // wait for their completion
        for (Thread t : threads)
        {
            try 
            {
                t.join();
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }

        swapBuffers();
    }
}
```

В мейне вызовим функцию для первого варианта, в которой создадим и инициализируем матрицу, а также выполним пару итераций.

```java
public static void main(String[] args)
{
    test1();
}

public static void test1()
{
    Grid grid = new Grid(1000);
    grid.randomize();
    grid.printResult();
    System.out.println("--------------------------------------------------");
    grid.performParallelIterations(1000, 3);
    grid.printResult();
}
```

Принт показывает лишь левую верхнюю секцию таблицы (10 на 10).

```
   1    1    0    1   -1    1    0    0   -1    1
   0    1   -1    1    1    0    1    0   -1   -1
   1    1   -1    1   -1    0    1   -1   -1   -1
   0   -1    0    0    1    0   -1   -1    0    0
   0   -1    0   -1    0    1   -1    0    0   -1
  -1    0    0    1   -1    1   -1    0   -1    1
  -1    1    1    0    1   -1    1    0    1   -1
  -1   -1   -1   -1    0    0    1    0    1    1
   0    0    0   -1   -1    0    1   -1    1    0
   0    1    0   -1   -1   -1   -1   -1   -1    1
--------------------------------------------------
  71   99  104   89   93   68   18  -64 -126 -141
  92  125  131  115  124   83   -4 -134 -227 -237
  46   59   72   80   97   46  -61 -195 -275 -262
 -15  -25   -5   25   46   -5 -104 -209 -253 -217
 -67  -89  -52   -1   24  -19  -91 -154 -163 -125
 -81 -104  -66  -16   11    2  -22  -38  -36  -14
 -90 -126 -109  -72  -39   -1   28   59   69   75
 -86 -143 -175 -176 -150  -69    9   82  102  105
 -87 -167 -245 -288 -280 -187  -77   26   70   73
 -80 -160 -253 -320 -335 -251 -126   -7   52   46
```

## Второй вариант: треды координируются сами

Для выполнения этого варианта, будем локально создавать общий временный объект, который будет служить монитором, счетчиком для оставшегося количества итераций, а также счетчиком общего и текущего количества тредов. Будем передовать контекстам треда этот общий объект, который они будут использовать для координации. 

В основной функции подсчета в гриде не будем проводить итераций: просто создадим наши контексты тредов, сами треды и их запустим. 

Когда один из тредов закончит свои подсчеты в текущей итерации, он синхронно увеличит число завершивших работу тредов в объекте-координаторе, а потом уснет до тех пор, пока все треды не завершат работу. Если тред был последним завершившим работу, он, вместо того, чтобы уснуть, разбудит всех спящих. Повторяем, пока остаточное число циклов не обнулится.

Для начала создадим класс объекта-координатора. Решил сделать этот объект просто вспомогательной структурой с данными, с которой будут работать треды.

```java
final class Coordinator
{
    public final int totalThreadCount;
    public int itersLeft;
    public int runningThreadCount;

    public Coordinator(int totalThreadCount, int itersLeft) 
    {
        this.totalThreadCount = totalThreadCount;
        this.itersLeft = itersLeft;
        this.runningThreadCount = totalThreadCount;
    }
}
```

В конструктор контекста добавим этого координатора, а также обновим его основной метод, как описано выше.

```java
class ThreadContext2 implements Runnable 
{
    private Coordinator coordinator;
    private Grid2 parent;
    private int start;
    private int share;

    public ThreadContext2(Coordinator coordinator, Grid2 parent, int start, int share)
    {
        this.coordinator = coordinator;
        this.parent = parent;
        this.start = start;
        this.share = share;
    }

    @Override
    public void run() 
    {
        while (true)
        {
            for (int i = 0; i < share; i++)
            {
                parent.storeSumAt(start + i);
            }

            synchronized (coordinator)
            {
                coordinator.runningThreadCount--;

                if (coordinator.runningThreadCount == 0)
                {
                    parent.swapBuffers();
                }
                
                if (coordinator.itersLeft > 1)
                {
                    if (coordinator.runningThreadCount == 0)
                    {
                        if (coordinator.runningThreadCount == 0)
                        {
                            coordinator.itersLeft--;
                            coordinator.runningThreadCount = coordinator.totalThreadCount;
                            coordinator.notifyAll();
                        }
                    }
                    else 
                    {
                        try
                        {
                            coordinator.wait();
                        }
                        catch (InterruptedException e) 
                        {
                            e.printStackTrace();
                        }
                    } 
                }
                else
                {
                    return;
                }
            }
        }
    }
}
```

В методе суммирования в гриде убираем цикл и корректно инициализируем координатора (показывать не буду, так как там все просто, смотрите файлы лабы).

Результаты схожие:

```
   1    1   -1    0    0   -1    0    1    1   -1
   1    1   -1    0    0    1    0   -1    0    1
   0    0    0   -1   -1    1   -1   -1   -1   -1
  -1   -1    1    1    0    1   -1    0    0    0
  -1    0    0   -1   -1    1   -1    1   -1    0
  -1    1    1    0    1   -1    0   -1    1    0
   1   -1    1    1    0    0    1    0    0    1
  -1    0    1    1    0    0    1    1    0   -1
   0    0    1    0    1    0   -1   -1   -1    0
   0   -1    0    1    1   -1   -1    0    1    1
--------------------------------------------------
  46   39    0  -37  -35  -32  -32  -44  -55  -72
  46   30  -18  -63  -56  -62  -75 -114 -140 -175
   6  -10  -26  -45  -37  -68 -105 -171 -204 -248
 -32  -41  -23  -18  -18  -64 -106 -175 -211 -268
 -29   -8   44   65   43  -14  -54  -99 -127 -180
   3   57  125  149  107   49   11  -16  -49 -102
  30  115  206  242  184  111   55   32   -3  -44
  26  117  222  271  207  112   34    4  -22  -53
  -4   71  180  235  169   56  -23  -37  -24  -34
 -37    5   95  140   84  -23  -74  -63   -8   -6
```

## Обновление 1

Можем печатать промежуточный результат. Для этого, изменим код в ифе последнего треда:
```java
if (coordinator.runningThreadCount == 0)
{
    parent.printResult();
    System.out.println("--------------------------------------------------");

    parent.swapBuffers();

    if (coordinator.itersLeft == 1)
    {
        parent.printResult();
    }
}
```

А в мейне, не будем печатать буфер:
```java
public static void test2()
{
    Grid2 grid = new Grid2(1000);
    grid.randomize();
    grid.performParallelIterations(1000, 3);
}
```

Результат:
```
  -1    1   -1    0   -1   -1    0   -1   -1    0 
   0    1   -1    1   -1    1    0   -1    1    1 
  -1    1   -1    0   -1    1    0    0   -1   -1 
   0    1    1    1    1   -1    1   -1   -1    1 
   1    1    1    1    1    1    0    0   -1   -1 
   0   -1    0   -1   -1   -1   -1    1    0    0 
   1    1    0    0   -1   -1    1   -1   -1   -1 
   0    0   -1    1   -1    1    0    0    1   -1 
   1    1   -1    0    0    0    0    1   -1    1 
  -1   -1    1    0    0    1   -1    1   -1    1 
--------------------------------------------------
   1   -1    1   -3   -1   -2   -2   -2   -1    0 
   1   -2    1   -5   -1   -2   -1   -3   -3   -3 
   2    1    4    0    2    1    0   -2   -2    0 
   3    4    6    4    4    3    1   -3   -5   -3 
   2    4    4    4    1    0   -1   -2   -2    0 
   3    4    2    0   -2   -2   -1   -2   -4   -3 
   1    0   -1   -4   -4   -4   -1    0   -2   -2 
   4    2    1   -3   -1   -1    1    0   -2   -1 
   0   -1    0   -1    2    0    3    0    2    0 
  -2   -1   -1    0    1    2    5    1    2    0 
--------------------------------------------------
  -1    1   -9   -8  -14   -9  -12  -12  -12   -8 
   2    8   -4   -2  -11   -6  -13  -16  -16   -7 
   9   20   13   15    6    7   -6  -18  -24  -12 
  16   30   31   29   19   11   -3  -16  -19   -5 
  20   32   32   23   12    3   -7  -19  -24  -15 
  14   19   13    0  -11  -14  -13  -15  -17  -17 
  14   16    1  -12  -21  -15  -10  -11  -16  -20 
   6    6   -7  -11  -16   -5   -2    1   -5   -8 
   2    2   -4   -2   -1   12   11   12    2    4 
 -13  -18  -13   -4    5   19   18   20    9   11 
--------------------------------------------------
  10   -3  -14  -48  -50  -65  -68  -81  -71  -48 
  39   39   34  -14  -22  -58  -85 -129 -125  -81 
  85  125  140   96   68    4  -60 -131 -133  -69 
 127  203  225  180  125   42  -48 -136 -152  -83 
 131  207  209  148   72   -3  -73 -133 -147  -91 
 115  161  124   37  -35  -76 -101 -132 -154 -125 
  75   82   25  -64 -105 -107  -84  -88 -108 -109 
  46   36  -11  -73  -71  -47   -7  -18  -41  -60 
 -15  -39  -51  -53   -3   41   86   66   46   10 
 -75 -115 -106  -56   31  102  147  121   97   63 
```


## Выводы

Если бы дали задание зарефакторить этот код, разделил бы почетче сообщения между классами (кто кем владеет и к кому обращается), потому что в текущем коде все обращаются друг к другу и не особо понятно кто главный. 

Если хорошенько подумать, то и объект координатора тут не нужен, поскольку можно было бы хранить все необходимые данные в классе грида и использовать его в качестве монитора.

Еще нашел в интернете, что существуют вспомогательные классы для работы с потоками в неймспейсе `java.util.concurrent`, и что рекомендуют использовать их, дабы не переизобретать велосипед.