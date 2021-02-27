public class Humanoid extends Creature {
    protected Hand leftHand;
    protected Hand rightHand;
    protected Armor armor;

    public Humanoid() 
    {
    }

    public Humanoid(Vector2 position, int mass, int health, int damage, Hand leftHand, Hand rightHand, Armor armor) 
    {
        super(position, mass, health, damage);
        this.leftHand = leftHand;
        this.rightHand = rightHand;
        this.armor = armor;
    }

    @Override
    public int getMass() {
        return this.mass + armor.getMass();
    }

    @Override
    public int getDamage() {
        int damage = this.damage;
        if (leftHand != null && leftHand.isMetallic()) {
            damage += 1;
        }
        if (rightHand != null && rightHand.isMetallic()) {
            damage += 1;
        }
        return damage;
    }

    @Override
    public void beAttacked(int damage) {
        super.beAttacked(this.armor.blockDamage(damage));
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
            leftHand = new Hand();
            Entity.autoId(leftHand);
            leftHand.promptInput();
        }

        if (Prompt.bool("right hand?", "yes", "no"))
        {
            rightHand = new Hand();
            Entity.autoId(rightHand);
            rightHand.promptInput();
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
            leftHand = new Hand();
            Entity.autoId(leftHand);
            leftHand.randomize();
        }

        if (random.nextBoolean())
        {
            rightHand = new Hand();
            Entity.autoId(rightHand);
            rightHand.randomize();
        }

        if (random.nextBoolean())
        {
            armor = new Armor();
            Entity.autoId(armor);
            armor.randomize();
        }
    }
}
