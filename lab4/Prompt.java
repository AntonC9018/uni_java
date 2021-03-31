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
                String str = scanner.nextLine();
                int result = Integer.parseInt(str);
                return result;
            }
            catch (NumberFormatException exc)
            {
                System.out.println("That's not a number.");
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
                System.out.printf("Enter %s (%d <= x <= %d): ", message, min, max);
                String str = scanner.nextLine();
                result = Integer.parseInt(str);
                
                if (result < min)
                {
                    hasErrors = true;
                    System.out.printf("The number must be >= %d\n", min);
                }
                else if (result > max)
                {
                    hasErrors = true;
                    System.out.printf("The number must be <= %d\n", max);
                }
                else
                {
                    hasErrors = false;
                }
            }
            catch (NumberFormatException exc)
            {
                System.out.println("That's not a number.");
                hasErrors = true;
            }
            
        }
        while (hasErrors);
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
                System.out.printf("Enter %s: ", message);
                String str = scanner.nextLine();
                result = Integer.parseInt(str);
                if (result <= 0)
                {
                    System.out.printf("The number must be > 0\n");
                    hasErrors = true;
                }
                else 
                {
                    hasErrors = false;
                }
            }
            catch (NumberFormatException exc)
            {
                System.out.println("That's not a number.");
                hasErrors = true;
            }
        }
        while (hasErrors);
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
                System.out.printf("Enter %s: ", message);
                String str = scanner.nextLine();
                result = Integer.parseInt(str);
                if (result < 0)
                {
                    System.out.printf("The number must be >= 0\n");
                    hasErrors = true;
                }
                else
                {
                    hasErrors = false;
                }
            }
            catch (NumberFormatException exc)
            {
                System.out.println("That's not a number.");
                hasErrors = true;
            }
        }
        while (hasErrors);
        return result;
    }

    public static boolean bool(String message, String trueString, String falseString)
    {
        while (true)
        {
            System.out.printf("Enter %s (\"%s\" or \"%s\"): ", message, trueString, falseString);
            String input = scanner.nextLine().toLowerCase();
            if (!input.equals(trueString) && !input.equals(falseString))
            {
                System.out.printf("\"%s\" is not a valid option.\n", input);
            }
            else 
            {
                return input.equals(trueString);
            }
        }
    }

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
}
