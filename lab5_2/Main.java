public class Main 
{
    public static void main(String[] args)
    {
        test2();
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

    public static void test2()
    {
        Grid2 grid = new Grid2(1000);
        grid.randomize();
        grid.printResult();
        System.out.println("--------------------------------------------------");
        grid.performParallelIterations(1000, 3);
        grid.printResult();
    }

    // public static int[] 
}