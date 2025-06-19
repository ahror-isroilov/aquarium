package model;

import static common.Utils.*;
import static log.Logger.log;
import static log.Logger.warn;

public class Fish extends Thread {
    private final String FID;
    private final Gender gender;
    private final int lifespan;
    private final Position position;
    private final Aquarium aquarium;
    private final long birthtime;
    private String lineage = "";
    private int depth = 0;

    public Fish(Gender gender, Position position, Aquarium aquarium) {
        this.FID = genId(gender);
        this.lifespan = random.nextInt(20, 40);
        this.gender = gender;
        this.position = position;
        this.aquarium = aquarium;
        this.lineage = "🐟(" + gender(gender) + this.FID + ")";
        this.birthtime = System.currentTimeMillis();
    }

    public Fish(Gender gender, Position position, Aquarium aquarium, Fish parent1, Fish parent2) {
        this(gender, position, aquarium);
        setLineage(gender, parent1, parent2);
    }

    /**
     * Baliqning asosiy hayot sikli. 
     * Har 500ms da tasodifiy harakatlanadi va umri tugaganda o'ladi.
     */
    @Override
    public void run() {
        while (aquarium.isRunning()) {
            long elapsedSeconds = (System.currentTimeMillis() - birthtime) / 1000;
            if (elapsedSeconds >= lifespan) {
                break;
            }
            moveRandomly();
            log("🐟(%s)%s moved to %s".formatted(gender.getSymbol(), FID, position), "movements.log");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        warn("🐟(%s)%s died [All: %s]".formatted(gender.getSymbol(), FID, aquarium.getFishList().size()));
        aquarium.removeFish(this);
    }

    public Gender getGender() {
        return gender;
    }

    public int getLifespan() {
        return lifespan;
    }

    public Position getPosition() {
        return position;
    }

    public String getLineage() {
        return lineage;
    }

    public String getFID() {
        return FID;
    }

    public int getDepth() {
        return depth;
    }

    private String gender(Gender gender) {
        return (gender == Gender.MALE ? "♂-" : "♀-");
    }

    private void moveRandomly() {
        int deltaX = randomDelta();
        int deltaY = randomDelta();
        int deltaZ = randomDelta();
        position.x = checkBounds(position.x + deltaX, 0, aquarium.getX() - 1);
        position.y = checkBounds(position.y + deltaY, 0, aquarium.getY() - 1);
        position.z = checkBounds(position.z + deltaZ, 0, aquarium.getZ() - 1);
    }

    /**
     * Baliqning avlodini shakllantirish uchun method.
     * Tree ko'rinishida avldoni shakllantiradi.
     */
    private void setLineage(Gender gender, Fish parent1, Fish parent2) {
        if (parent1.getLineage().isEmpty()) {
            this.depth = 1;
            this.lineage = "🐟(" + gender(gender) + parent1.getFID() + ")\n" +
                    "└── 🧬[" + gender(parent1.gender) + parent1.getFID() + " ⥈ " + gender(parent2.gender) + parent2.getFID() + "] in " + parent1.getPosition() + "\n" +
                    "    └── 🐟(" + gender(gender) + this.FID + ")";
        } else {
            this.depth = parent1.getDepth() + 2;
            String indent1 = "    ".repeat(parent1.getDepth());
            String indent2 = "    ".repeat(parent1.getDepth() + 1);
            this.lineage =
                    parent1.getLineage() + "\n" +
                            indent1 + "└── 🧬[" + gender(parent1.gender) + parent1.getFID() + " ⥈ " + gender(parent2.gender) + parent2.getFID() + "]" + parent1.getPosition() + "\n" +
                            indent2 + "└── 🐟(" + gender(gender) + this.FID + ")";
        }
    }
}
