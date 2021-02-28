public class Humanoid extends Creature 
{
    protected Arm leftArm;
    protected Arm rightArm;
    protected Armor armor;

    public Humanoid() 
    {
    }

    public Humanoid(Vector2 position, int mass, int health, int damage, Arm leftHand, Arm rightHand, Armor armor) 
    {
        super(position, mass, health, damage);
        this.leftArm = leftHand;
        this.rightArm = rightHand;
        this.armor = armor;
    }

    @Override
    public int getMass() 
    {
        return this.mass + armor.getMass();
    }

    @Override
    public int getDamage() 
    {
        int damage = super.getDamage();
        if (leftArm != null && leftArm.isMetallic()) 
        {
            damage += 1;
        }
        if (rightArm != null && rightArm.isMetallic()) 
        {
            damage += 1;
        }
        return damage;
    }

    @Override
    public void beAttacked(int damage) 
    {
        if (this.armor != null)
        {
            damage = this.armor.blockDamage(damage);
        }
        super.beAttacked(damage);
    }

    public Armor equipArmor(Armor armor) {
        Armor t = this.armor;
        this.armor = armor;
        return t;
    }

    @Override
    public void promptInput() 
    {
        super.promptInput();
        // This can be cleaned up with templates
        // That is Entity.promptCreate<Hand>(); or stuff like that
        // or with code generation (by a preprocessor program).
        if (Prompt.bool("left hand?", "yes", "no"))
        {
            leftArm = new Arm();
            Entity.autoId(leftArm);
            leftArm.promptInput();
        }

        if (Prompt.bool("right hand?", "yes", "no"))
        {
            rightArm = new Arm();
            Entity.autoId(rightArm);
            rightArm.promptInput();
        }

        if (Prompt.bool("armor?", "yes", "no"))
        {
            armor = new Armor();
            Entity.autoId(armor);
            armor.promptInput();
        }
    }

    @Override
    public void randomize()
    {
        super.randomize();
        
        if (random.nextBoolean())
        {
            leftArm = new Arm();
            Entity.autoId(leftArm);
            leftArm.randomize();
        }

        if (random.nextBoolean())
        {
            rightArm = new Arm();
            Entity.autoId(rightArm);
            rightArm.randomize();
        }

        if (random.nextBoolean())
        {
            armor = new Armor();
            Entity.autoId(armor);
            armor.randomize();
        }
    }
}
