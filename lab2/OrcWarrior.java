public class OrcWarrior extends Orc implements IDashing
{
    public OrcWarrior()
    {
    }

    public OrcWarrior(Vector2 position, int mass, int health, int damage, Hand leftHand, Hand rightHand, Armor armor) 
    {
        super(position, mass, health, damage, leftHand, rightHand, armor);
    }

    @Override
    public void dash(Creature creature) {
        // We're able to dash only if on the same row or column
        if (this.position.x == creature.position.x || this.position.y == creature.position.y)
        {
            Vector2 vec = new Vector2(
                creature.position.x - this.position.x, 
                creature.position.y - this.position.y
            );

            move(vec); attack(creature);
        }
    }
}
