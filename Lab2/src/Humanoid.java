public class Humanoid extends Creature 
{
    protected Arm leftArm;
    protected Arm rightArm;
    protected Armor armor;

    public Humanoid() 
    {
    }

    public Humanoid(Vector2 position, int mass, int health, int damage, Arm leftArm, Arm rightArm, Armor armor) 
    {
        super(position, mass, health, damage);
        this.leftArm = leftArm;
        this.rightArm = rightArm;
        this.armor = armor;
    }

    @Override
    public int getMass() 
    {
        int mass = super.getMass();
        if (armor != null)
        {
            mass += armor.getMass();
        }
        return mass;
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
        // This can be cleaned up with generics
        // That is Entity.promptCreate<Arm>(); or stuff like that
        // or with code generation (by a preprocessor program).
        if (Prompt.bool("left arm?", "yes", "no"))
        {
            leftArm = new Arm();
            Entity.autoId(leftArm);
            leftArm.promptInput();
        }

        if (Prompt.bool("right arm?", "yes", "no"))
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
