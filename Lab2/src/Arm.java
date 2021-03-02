public class Arm extends Entity
{
    private boolean metallic;

    public Arm(){}

    public Arm(boolean metallic)
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
        metallic = Prompt.bool("arm's material", "metal", "normal");
    }

    @Override
    public void randomize() 
    {
        metallic = random.nextBoolean();
    }
}
