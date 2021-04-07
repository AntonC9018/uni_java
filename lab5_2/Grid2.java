import java.util.Random;

public final class Grid2
{
    private int[][] primaryBuffer;
    private int[][] secondaryBuffer;

    public final int dimension()
    {
        return primaryBuffer.length;
    }

    public Grid2(int dimension)
    {
        primaryBuffer   = createBuffer(dimension);
        secondaryBuffer = createBuffer(dimension);
    }

    private final int[][] createBuffer(int dimension)
    {
        int[][] buffer = new int[dimension][];
        
        for (int i = 0; i < dimension; i++)
        {
            buffer[i] = new int[dimension];
        }

        return buffer;
    }

    public final void randomize()
    {
        Random random = new Random();
        for (int i = 0; i < primaryBuffer.length; i++)
        {
            for (int j = 0; j < primaryBuffer[0].length; j++)
                primaryBuffer[i][j] = random.nextInt(3) - 1;
        }
    }

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

    public final void swapBuffers()
    {
        int[][] t = primaryBuffer;
        primaryBuffer = secondaryBuffer;
        secondaryBuffer = t;
    }

    public final void performParallelIterations(int numThreads, int numIters)
    {
        ThreadContext2[] contexts = new ThreadContext2[numThreads];
        int numElements = dimension() * dimension();
        double sharePerThread = (double)numElements / numThreads;
        double currentStart = 0;
        Coordinator coordinator = new Coordinator(numThreads, numIters);
        
        for (int i = 0; i < numThreads - 1; i++)
        {
            double nextStart = Math.ceil(sharePerThread * (i + 1));
            int start = (int)(currentStart);
            int share = (int)(nextStart - currentStart);
            currentStart = nextStart;
            contexts[i] = new ThreadContext2(coordinator, this, start, share);
        }
        
        // account for possible floating point errors
        {
            int lastStart = (int)currentStart;
            int lastShare = (int)numElements - lastStart;
            contexts[numThreads - 1] = new ThreadContext2(coordinator, this, lastStart, lastShare);
        }
            
        Thread threads[] = new Thread[numThreads];

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
    }

    private static final int maxWidthToPrint = 10;

    public final void printResult() 
    {
        int width = Math.min(maxWidthToPrint, dimension());

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < width; j++)
                System.out.printf("%4d ", primaryBuffer[i][j]);
            
            System.out.println();
        }
    }
}

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
                        if (coordinator.itersLeft > 1)
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