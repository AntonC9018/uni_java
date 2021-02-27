public class Main 
{
    public static void main(String[] args)
    {
        // Constructors with parameters
        OrcWarrior orcWarrior_1 = new OrcWarrior(
            new Vector2(1, 1), 5, 5, 2, new Hand(false), new Hand(false), null
        );
        OrcWarrior orcWarrior_2 = new OrcWarrior(
            new Vector2(1, 5), 2, 2, 1, new Hand(true), new Hand(true), new Armor(1, 3)
        );
        Orc orc_1 = new Orc(
            new Vector2(1, 1), 5, 5, 1, null, null, new Armor(1, 3)
        );
        Orc orc_2 = new Orc(
            new Vector2(1, 1), 5, 5, 1, new Hand(false), new Hand(false), new Armor(0, 3)
        );
        Humanoid humanoid = new Humanoid(
            new Vector2(5, 5), 1, 1, 1, new Hand(false), new Hand(false), new Armor(0, 1)
        );
        Creature creature = new Creature(new Vector2(2, 2), 1, 1, 1);

        // Some randomized orcs
        OrcWarrior randomOrcWarrior = new OrcWarrior();
        randomOrcWarrior.randomize();
        Orc randomOrc = new Orc();
        randomOrc.randomize();

        Gson gson = new Gson();
        String json = gson.toJson(obj); 
    }
}
