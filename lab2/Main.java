import java.util.Arrays;

public class Main 
{
    public static void main(String[] args)
    {
        // Constructors with parameters
        OrcWarrior orcWarrior_1 = new OrcWarrior(
            // position, mass, health, damage, left arm, right arm, armor
            new Vector2(1, 1), 5, 5, 2, new Arm(false), new Arm(false), null
        );
        OrcWarrior orcWarrior_2 = new OrcWarrior(
            new Vector2(1, 5), 2, 2, 1, new Arm(true), new Arm(true), new Armor(1, 3)
        );
        Orc orc_1 = new Orc(
            new Vector2(1, 1), 5, 5, 2, null, null, new Armor(1, 3)
        );
        Orc orc_2 = new Orc(
            new Vector2(1, 1), 5, 5, 1, new Arm(false), new Arm(false), new Armor(0, 3)
        );
        Humanoid humanoid = new Humanoid(
            new Vector2(5, 5), 1, 1, 1, new Arm(false), new Arm(false), new Armor(0, 1)
        );
        Creature creature = new Creature(
            // position, mass, health, damage
            new Vector2(2, 2), 1, 1, 1
        );

        // Some randomized orcs
        OrcWarrior randomOrcWarrior = new OrcWarrior();
        randomOrcWarrior.randomize();
        // Assign an id automatically.
        // The randomizer does not assign an id to the top-level object
        Entity.autoId(randomOrcWarrior);

        Orc randomOrc = new Orc();
        randomOrc.randomize();
        Entity.autoId(randomOrc);


        // make sure both orc_1 and orc_2 are not dead by default 
        assert(orc_1.isAlive());
        assert(orc_2.isAlive());

        // save the current health
        int orc2_hp = orc_2.getHealth();
        // make orc 1 attack orc 2
        orc_1.attack(orc_2);
        // make sure orc 2 took damage (he has armor block value 0)
        assert(orc_2.getHealth() < orc2_hp);

        // save the current positions (note: creates a copy)
        Vector2 orc1_pos = orc_1.getPosition();
        // make orc 1 move 1 to the right
        orc_1.move(new Vector2(1, 0));
        // make sure orc_1 has moved
        assert(orc1_pos.x - orc1_pos.x == 1);

        // query the current attack damage
        int orc1_dmg = orc_1.getDamage();
        // enrage the orc, attack damage increased
        orc_1.enrage();
        // make sure it is bigger than before
        assert(orc_1.getDamage() > orc1_dmg);
        // make sure it is enraged
        assert(orc_1.isEnraged());
        // calm him down (damage restored to normal)
        orc_1.calmDown();
        // make sure damage gets back to normal
        assert(orc_1.getDamage() == orc1_dmg);

        // make the humanoid attack the creature until it's dead
        while (creature.isAlive())
        {
            humanoid.attack(creature);
        }
        // Now if we tried to attack the humanoid with the creature, 
        // it wouldn't work because the creature is dead
        creature.tryAttack(humanoid);
        // This will work though. This one is unconditional
        creature.attack(humanoid);

        // humanoids may reequip armor
        Armor new_armor = new Armor(5, 5);
        // unequips the current armor into humanoid_armor1 and equips the new armor
        Armor humanoid_armor_1 = humanoid.equipArmor(new_armor);
        // unequips the new armor into humanoid_armor2 and puts on the old armor
        Armor humanoid_armor_2 = humanoid.equipArmor(humanoid_armor_1);
        assert(humanoid_armor_2 == new_armor);

        // make two people at the same position
        Humanoid human_1 = new Humanoid(new Vector2(1, 1), 1, 1, 1, null, null, null);
        Humanoid human_2 = new Humanoid(new Vector2(1, 1), 1, 1, 1, null, null, null);
        // make sure this function works
        assert(human_1.isAtSamePoisition(human_2));
        // check again with a different positoin
        Humanoid human_3 = new Humanoid(new Vector2(2, 1), 1, 1, 1, null, null, null);
        assert(!human_1.isAtSamePoisition(human_3));

        // unequip the current armor of orc_1
        orc_1.equipArmor(null);
        // store their mass without the armor
        int orc1_mass = orc_1.getMass();
        // equip armor with mass of 1
        orc_1.equipArmor(new Armor(0, 1));
        // make sure the mass increased by one
        assert(orc1_mass == orc_1.getMass() - 1);

        // Print to the console
        orc_1.print();
        // Serialize to a file
        orc_1.serializeTo("orc.txt");

        // An array with these entities
        Entity[] entities = {
            orcWarrior_1,
            orcWarrior_2,
            orc_1,
            orc_2,
            humanoid,
            creature,
            randomOrcWarrior,
            randomOrc,
            new Arm(false) // also includes an unattackable arm
        };

        // Need to show all of them to the screen
        System.out.println("Printing all entities:");
        for (Entity e : entities)
        {
            System.out.printf("Classname: %s\n", e.getClass().getName());
            e.print();
        }

        // An an array of just attackable ones
        IAttackable[] attackables = 
            Arrays.stream(entities)
                .filter(e -> e instanceof IAttackable)
                .toArray(IAttackable[]::new);

        assert(entities.length - 1 == attackables.length);

        // Streams keep the order
        assert(attackables[0] == orcWarrior_1);
        // Attack the first attackable with 10 damage
        attackables[0].beAttacked(10);
        // It will die (since it has 5 hp)
        assert(((Creature)attackables[0]).isAlive() == false);

        // An array of just the ones that can dash, and are alive
        IDashing[] dashings = Arrays.stream(entities)
            .filter(e -> e instanceof Creature && e instanceof IDashing)
            .filter(e -> ((Creature)e).isAlive())
            .toArray(IDashing[]::new);

        // It can dash
        dashings[0].dash(orc_1);

        // Create an empty creature and prompt input
        OrcWarrior warrior = new OrcWarrior();
        warrior.promptInput();
        // To properly set up the orc, an id has to be assigned
        Entity.autoId(warrior);

        // write it to a file
        warrior.serializeTo("warrior.txt");
    }
}
