import java.util.InputMismatchException;
import java.util.Scanner;

public final class Prompt 
{
    private Prompt(){}

    public static Scanner scanner = new Scanner(System.in);

    public static int int_(String message)
    {
        while (true)
        {
            try
            {
                System.out.printf("Enter %s: ", message);
                return scanner.nextInt();
            }
            catch (InputMismatchException exc)
            {
                System.out.println("Input Mismatch!");
            }
        }
    }

    
    public static int int_(String message, int min, int max)
    {
        int result = 0;
        boolean hasErrors;

        do
        {
            try
            {
                System.out.printf("Enter %s (must be between %d and %d): ", message, min, max);
                result = scanner.nextInt();
                hasErrors = false;
            }
            catch (InputMismatchException exc)
            {
                System.out.println("Input Mismatch!");
                hasErrors = true;
                continue;
            }
        }
        while (hasErrors || result >= min && result <= max);
        return result;
    }
    
    public static int positiveInt(String message)
    {
        int result = 0;
        boolean hasErrors;

        do
        {
            try
            {
                System.out.printf("Enter %s (must be positive): ", message);
                result = scanner.nextInt();
                hasErrors = false;
            }
            catch (InputMismatchException exc)
            {
                System.out.println("Input Mismatch!");
                hasErrors = true;
                continue;
            }
        }
        while (hasErrors || result <= 0);
        return result;
    }

    public static int positiveOrZeroInt(String message)
    {
        int result = 0;
        boolean hasErrors;

        do
        {
            try
            {
                System.out.printf("Enter %s (must be positive or 0): ", message);
                result = scanner.nextInt();
                hasErrors = false;
            }
            catch (InputMismatchException exc)
            {
                System.out.println("Input Mismatch!");
                hasErrors = true;
                continue;
            }
        }
        while (hasErrors || result < 0);
        return result;
    }

    public static boolean bool(String message, String trueString, String falseString)
    {
        String input;

        do
        {
            System.out.printf("Enter %s (%s or %s): ", message, trueString, falseString);
            input = scanner.nextLine().toLowerCase();
        }
        while (input != trueString && input != falseString);
        return input == trueString;
    }
}
