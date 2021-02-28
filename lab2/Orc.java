public class Orc extends Humanoid 
{
    protected int rage;
    protected static int rageIncrease = 1;
    
    public Orc()
    {
    }

    public Orc(Vector2 position, int mass, int health, int damage, Arm leftArm, Arm rightArm, Armor armor) 
    {
        super(position, mass, health, damage, leftArm, rightArm, armor);
    }
    
    public final boolean isEnraged()
    {
        return rage > 0;
    }
    
    public void enrage()
    {
        rage += rageIncrease;
    }

    public void calmDown()
    {
        rage = 0;
    }

    @Override
    public int getDamage()
    {
        return super.getDamage() + rage;
    }
}
