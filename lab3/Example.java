public class Example
{
    public static void main(String[] args)
    {
        feed(ValueBound.lower - 1);
        feed(ValueBound.upper + 1);
        feed((ValueBound.lower + ValueBound.upper) / 2);
    }
    
    public static void feed(int value)
    {
        try
        {
            if (value < ValueBound.lower)
            {
                throw new ValueLowException(value);
            }
            if (value > ValueBound.upper)
            {
                throw new ValueHighException(value);
            }

            System.out.printf("Nom-nom... Delicious %d.\n", value);
        }
        catch(ValueException exc)
        {
            exc.handle();
            System.out.printf("Eugh... can't eat this stuff, sorry :/\n", value);
        }
    }
}

final class ValueBound
{
    private ValueBound(){}
    static final int lower = 5;
    static final int upper = 10;
}

abstract class ValueException extends RuntimeException
{
    int value;
    ValueException(int value) { this.value = value; }
    abstract void handle();
}

class ValueLowException extends ValueException
{
    ValueLowException(int value) { super(value); }

    @Override
    public void handle()
    {
        System.out.printf(
            "The value %d is too low. It must be no less than %d.\n", 
            value, ValueBound.lower
        );
    }
}

class ValueHighException extends ValueException
{
    ValueHighException(int value) { super(value); }

    @Override
    public void handle()
    {
        System.out.printf(
            "The value %d is too high. It must be no more than %d.\n", 
            value, ValueBound.upper
        );
    }
}