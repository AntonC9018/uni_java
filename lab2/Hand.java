public class Hand extends Entity
{
    private boolean metallic;

    public Hand(){}

    public Hand(boolean metallic)
    {
        super();
        Entity.autoId(this);
        this.metallic = metallic;
    }

    public final boolean isMetallic()
    {
        return metallic;   
    }

    @Override
    public void promptInput() 
    {
        metallic = Prompt.bool("hand's material", "metal", "normal");
    }

    @Override
    public void randomize() 
    {
        metallic = random.nextBoolean();
    }
}
