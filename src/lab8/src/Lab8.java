import java.util.ArrayList;


interface Attacker {
    int attack(GameCharacter target);
}

interface Supporter {
    int heal(GameCharacter target);
}

// 1. Soyut Sınıf (Abstract Class)
abstract class GameCharacter {
    private String name;
    private int level;
    private int health;
    private int maxHealth;

    public GameCharacter(String name, int level, int maxHealth) {
        this.name = name;

        if (level < 1 || level > 100) {
            System.out.println("Warning: Invalid level (" + level + ") for " + name + ". Setting level to 1.");
            this.level = 1;
        } else {
            this.level = level;
        }

        if (maxHealth <= 0) {
            System.out.println("Warning: Invalid maxHealth (" + maxHealth + ") for " + name + ". Setting maxHealth to 100.");
            this.maxHealth = 100;
        } else {
            this.maxHealth = maxHealth;
        }

        // Yeni karakter tam can ile başlar
        this.health = this.maxHealth;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    protected void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
        } else if (health > this.maxHealth) {
            this.health = this.maxHealth;
        } else {
            this.health = health;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int takeDamage(int damage) {
        if (damage <= 0 || !isAlive()) {
            return 0;
        }
        int previousHealth = this.health;
        setHealth(this.health - damage);
        return previousHealth - this.health;
    }

    public int restoreHealth(int amount) {
        // Yenilmiş (canı 0 olan) karakter iyileştirilemez
        if (!isAlive() || amount <= 0) {
            return 0;
        }
        int previousHealth = this.health;
        setHealth(this.health + amount);
        return this.health - previousHealth;
    }

    public abstract String getCharacterInfo();

    public abstract int calculatePower();
}

// 2a. Warrior Sınıfı (Sadece Attacker)
class Warrior extends GameCharacter implements Attacker {
    private int armor;

    public Warrior(String name, int level, int maxHealth, int armor) {
        super(name, level, maxHealth);
        this.armor = Math.max(0, armor);
    }

    public int getArmor() {
        return armor;
    }

    @Override
    public int calculatePower() {
        return getLevel() * 10 + armor * 2;
    }

    @Override
    public int takeDamage(int damage) {
        if (damage <= 0 || !isAlive()) {
            return 0;
        }
        // Gelen her darbeden armor / 2 düşülür, ancak en az 1 hasar alınır
        int reducedDamage = damage - (armor / 2);
        int effectiveDamage = Math.max(1, reducedDamage);
        return super.takeDamage(effectiveDamage);
    }

    @Override
    public int attack(GameCharacter target) {
        if (!isAlive()) {
            System.out.println(getName() + " is defeated and cannot attack.");
            return 0;
        }
        if (target == null || !target.isAlive()) {
            System.out.println(getName() + " cannot attack a defeated or null target.");
            return 0;
        }
        int damage = calculatePower() / 4;
        return target.takeDamage(damage);
    }

    @Override
    public String getCharacterInfo() {
        return "[Warrior] Name: " + getName()
                + " | Level: " + getLevel()
                + " | HP: " + getHealth() + "/" + getMaxHealth()
                + " | Armor: " + armor
                + " | Power: " + calculatePower()
                + " | Alive: " + isAlive();
    }
}

// 2b. Mage Sınıfı (Hem Attacker Hem Supporter)
class Mage extends GameCharacter implements Attacker, Supporter {
    private int mana;

    public Mage(String name, int level, int maxHealth, int mana) {
        super(name, level, maxHealth);
        this.mana = Math.max(0, mana);
    }

    public int getMana() {
        return mana;
    }

    @Override
    public int calculatePower() {
        return getLevel() * 8 + mana;
    }

    @Override
    public int attack(GameCharacter target) {
        if (!isAlive()) {
            System.out.println(getName() + " is defeated and cannot attack.");
            return 0;
        }
        if (target == null || !target.isAlive()) {
            System.out.println(getName() + " cannot attack a defeated or null target.");
            return 0;
        }
        // Power, mana harcanmadan önce hesaplanır
        int powerBeforeCost = calculatePower();
        int manaCost = 20;

        if (mana < manaCost) {
            System.out.println(getName() + " does not have enough mana to attack (needs " + manaCost + ", has " + mana + ").");
            return 0;
        }

        mana -= manaCost;
        int damage = powerBeforeCost / 3;
        return target.takeDamage(damage);
    }

    @Override
    public int heal(GameCharacter target) {
        if (!isAlive()) {
            System.out.println(getName() + " is defeated and cannot heal.");
            return 0;
        }
        if (target == null || !target.isAlive()) {
            return 0;
        }
        int manaCost = 15;
        if (mana < manaCost) {
            System.out.println(getName() + " does not have enough mana to heal (needs " + manaCost + ", has " + mana + ").");
            return 0;
        }

        mana -= manaCost;
        int healAmount = getLevel() * 2;
        return target.restoreHealth(healAmount);
    }

    @Override
    public String getCharacterInfo() {
        return "[Mage] Name: " + getName()
                + " | Level: " + getLevel()
                + " | HP: " + getHealth() + "/" + getMaxHealth()
                + " | Mana: " + mana
                + " | Power: " + calculatePower()
                + " | Alive: " + isAlive();
    }
}

// 2c. Healer Sınıfı (Sadece Supporter)
class Healer extends GameCharacter implements Supporter {
    private int healAmount;

    public Healer(String name, int level, int maxHealth, int healAmount) {
        super(name, level, maxHealth);
        this.healAmount = Math.max(0, healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public int calculatePower() {
        return getLevel() * 5 + healAmount;
    }

    @Override
    public int heal(GameCharacter target) {
        if (!isAlive()) {
            System.out.println(getName() + " is defeated and cannot heal.");
            return 0;
        }
        if (target == null || !target.isAlive()) {
            return 0;
        }
        int restored = healAmount + getLevel();
        return target.restoreHealth(restored);
    }

    @Override
    public String getCharacterInfo() {
        return "[Healer] Name: " + getName()
                + " | Level: " + getLevel()
                + " | HP: " + getHealth() + "/" + getMaxHealth()
                + " | HealAmount: " + healAmount
                + " | Power: " + calculatePower()
                + " | Alive: " + isAlive();
    }
}

// 4. Team Sınıfı
class Team {
    private ArrayList<GameCharacter> members;

    public Team() {
        this.members = new ArrayList<>();
    }

    public boolean addCharacter(GameCharacter character) {
        if (character == null) {
            System.out.println("Cannot add a null character to the team.");
            return false;
        }
        if (findByName(character.getName()) != null) {
            System.out.println("Warning: A character named '" + character.getName() + "' is already in the team. Duplicate names are not allowed.");
            return false;
        }
        members.add(character);
        return true;
    }

    public boolean removeCharacter(String name) {
        GameCharacter found = findByName(name);
        if (found != null) {
            members.remove(found);
            return true;
        }
        return false;
    }

    public GameCharacter findByName(String name) {
        if (name == null) {
            return null;
        }
        for (GameCharacter c : members) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public void attackAll(GameCharacter target) {
        if (target == null || !target.isAlive()) {
            System.out.println("Target is already defeated or invalid.");
            return;
        }
        for (GameCharacter member : members) {
            if (member.isAlive() && member instanceof Attacker) {
                int dealt = ((Attacker) member).attack(target);
                System.out.println("  -> " + member.getName() + " attacked " + target.getName() + " for " + dealt + " damage.");
            }
        }
    }

    public void healTeam() {
        for (GameCharacter member : members) {
            if (member.isAlive() && member instanceof Supporter) {
                GameCharacter lowestRatioTarget = null;
                double lowestRatio = Double.MAX_VALUE;

                for (GameCharacter candidate : members) {
                    if (candidate.isAlive()) {
                        double ratio = (double) candidate.getHealth() / candidate.getMaxHealth();
                        if (ratio < lowestRatio) {
                            lowestRatio = ratio;
                            lowestRatioTarget = candidate;
                        }
                    }
                }

                if (lowestRatioTarget != null) {
                    int restored = ((Supporter) member).heal(lowestRatioTarget);
                    System.out.println("  -> " + member.getName() + " healed " + lowestRatioTarget.getName() + " for " + restored + " HP.");
                }
            }
        }
    }

    public int getTotalPower() {
        int total = 0;
        for (GameCharacter member : members) {
            if (member.isAlive()) {
                total += member.calculatePower();
            }
        }
        return total;
    }

    public void displayTeam() {
        System.out.println("--- Team Members (Total Alive Power: " + getTotalPower() + ") ---");
        if (members.isEmpty()) {
            System.out.println("Team is empty.");
            return;
        }
        for (GameCharacter member : members) {
            System.out.println(member.getCharacterInfo());
        }
    }

    public ArrayList<GameCharacter> getMembers() {
        return members;
    }
}


public class Lab8 {
    public static void main(String[] args) {
        System.out.println("=== 1. TESTING INVALID INPUTS ===");
        Warrior invalidLevelWarrior = new Warrior("BrokenKnight", 150, 120, 10); // level > 100 -> 1 olur
        Healer invalidHpHealer = new Healer("BrokenCleric", 10, -20, 15);    // maxHealth <= 0 -> 100 olur
        System.out.println(invalidLevelWarrior.getCharacterInfo());
        System.out.println(invalidHpHealer.getCharacterInfo());

        System.out.println("\n=== 2. CREATING TEAM & DUPLICATE NAME TEST ===");
        Team team = new Team();
        Warrior warrior = new Warrior("Arthur", 12, 140, 18);
        Mage mage = new Mage("Merlin", 14, 100, 50);
        Healer healer = new Healer("Elara", 11, 110, 22);

        team.addCharacter(warrior);
        team.addCharacter(mage);
        team.addCharacter(healer);

      
        Warrior duplicateWarrior = new Warrior("Arthur", 5, 80, 5);
        team.addCharacter(duplicateWarrior);

      
        Warrior golem = new Warrior("Golem", 18, 220, 20);

        System.out.println("\n=== 3. INITIAL TEAM & ENEMY STATUS ===");
        team.displayTeam();
        System.out.println("Enemy: " + golem.getCharacterInfo());

        // En az 2 turluk savaş simülasyonu
        int rounds = 2;
        for (int round = 1; round <= rounds; round++) {
            System.out.println("\n=== ROUND " + round + " ===");

            // 1) Takım düşmana saldırır
            System.out.println("[Team Attacks Enemy]");
            team.attackAll(golem);
            System.out.println("Golem HP after team attack: " + golem.getHealth() + "/" + golem.getMaxHealth());

            // 2) Düşman takım üyelerine saldırır
            System.out.println("[Enemy Attacks Team]");
            if (golem.isAlive()) {
                for (GameCharacter member : team.getMembers()) {
                    if (member.isAlive()) {
                        int dmgTaken = golem.attack(member);
                        System.out.println("  -> " + golem.getName() + " attacked " + member.getName() + " for " + dmgTaken + " damage.");
                        break; // Her tur düşman bir takım üyesine vurur
                    }
                }
            }

      
            System.out.println("[Team Heals Itself]");
            team.healTeam();
        }

        System.out.println("\n=== 4. SEARCH, REMOVE & DISPLAY TEAM ===");
        GameCharacter searched = team.findByName("Merlin");
        System.out.println("Search result for 'Merlin': " + (searched != null ? searched.getCharacterInfo() : "Not found"));

        boolean removed = team.removeCharacter("Elara");
        System.out.println("Removed 'Elara': " + removed);

        team.displayTeam();
    }
}