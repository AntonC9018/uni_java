public class Main 
{
    public static void main(String args[]) 
    {
        test1();
        test2();
    }

    public static void test1() 
    {
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
        
        int totalCharacters = 0;
        for (int i = 0; i < matrix.length; i++)
        {
            totalCharacters += matrix[i].length;
        }
        
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
    }

    public static void test2() 
    {
        char matrix[][] = {
            {'-', '+', '/', '*', '%', '1', '1' },
            {'+', '+', '+', '+', '+', '+', '+' },
            {'-', '+', '/', '^', '%', '1', '/' },
            {'-', '+', 'h', '*', '%', '1', '1' },
            {'+', '+', '/', '+', '%', '+', '1' },
            {'-', '+', '+', '+', '%', '%', '%' }
        };
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
        
        int totalCharacters = 0;
        for (int i = 0; i < matrix.length; i++)
        {
            totalCharacters += matrix[i].length;
        }

        // wait for all threads to terminate
        for (int i = 0; i < threads.length; i++)
        {
            try 
            {
                threads[i].join();
                // counts.add(counters[i].getResult());
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }

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
    }
}
