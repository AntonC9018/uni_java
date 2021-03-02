public class Armor extends Entity 
    implements IWithMass
{
    private int block;
    private int mass;

    public Armor(){}

    public Armor(int block, int mass)
    {
        super();
        Entity.autoId(this);
        this.block = block;
        this.mass = mass;
    }

    public final int getMass()
    {
        return mass;
    }

    public final int blockDamage(int damage)
    {
        return Math.max(damage - block, 0);
    }

    @Override
    public void promptInput() 
    {
        block = Prompt.positiveInt("armor block value");
        mass  = Prompt.positiveInt("armor mass");
    }

    @Override
    public void randomize() 
    {
        block = random.nextInt(3);
        mass  = random.nextInt(5) + 1;
    }
}
