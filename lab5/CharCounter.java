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


class CharCounter2 implements Runnable 
{
    private char[] row;
    // private Counts counts;
    private Counts counts_monitor;

    public CharCounter2(char[] row, Counts counts_monitor)
    {
        this.row = row;
        this.counts_monitor = counts_monitor;
    }

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
}