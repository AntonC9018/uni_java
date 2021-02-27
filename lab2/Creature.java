public class Creature extends Entity 
    implements IWithMass, IAttackable, IMoving, IAttacking
{
    protected Vector2 position;
    protected int mass;
    protected int health;
    protected int damage = defaultCreatureAttackDamage;

    private final static int defaultCreatureAttackDamage = 1;

    public Creature()
    {
    }

    public Creature(Vector2 position, int mass, int health, int damage)
    {
        super();
        Entity.autoId(this);
        this.position = position;
        this.mass = mass;
        this.health = health;
        this.damage = damage;
    }

    public void attack(IAttackable attackable)
    {
        attackable.beAttacked(getDamage());
    }

    public final void tryAttack(Creature creature)
    {
        if (isAlive() && creature.isAlive())
        {
            attack(creature);
        }
    }

    public void beAttacked(int damage)
    {
        if (damage > 0)
        {
            health -= damage;
        }
    }

    public void move(Vector2 vec)
    {
        this.position.x += vec.x;
        this.position.y += vec.y;
    }

    public final boolean isAlive()
    {
        return health > 0;
    }

    public final Vector2 getPosition()
    {
        return new Vector2(position.x, position.y);
    }

    public int getHealth()
    {
        return health;
    }

    public int getMass()
    {
        return mass;
    }

    public int getDamage()
    {
        return damage;
    }

    public final boolean isAtSamePoisition(Creature creature)
    {
        return creature.position.x == this.position.x 
            && creature.position.y == this.position.y;
    }

    @Override
    public void promptInput() 
    {
        position = new Vector2(
            Prompt.int_("x position"),
            Prompt.int_("y position")
        );
        mass   = Prompt.positiveInt("mass");
        health = Prompt.positiveInt("health");
        damage = Prompt.positiveOrZeroInt("damage");
    }

    public void randomize()
    {
        position = new Vector2(random.nextInt(50), random.nextInt(50));
        mass   = random.nextInt(50) + 10;
        health = random.nextInt(5) + 2;
        damage = random.nextInt(3) + 1;
    }
}
