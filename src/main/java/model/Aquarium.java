package model;

import common.ReproductionManager;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static log.Logger.error;
import static log.Logger.log;

public final class Aquarium {
    private final int x;
    private final int y;
    private final int z;
    private final int capacity;
    private final Queue<Fish> fishList = new ConcurrentLinkedQueue<>();
    private final Random random = new Random();
    private volatile boolean running = true;

    public Aquarium(int x, int y, int z, int capacity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.capacity = capacity;
    }

    /**
     * Akvariumga baliq qo'shadigan method
     * @param fish qo'shilayotgan yangi baliq
     */
    public synchronized void addFish(Fish fish) {
        if (fishList.size() >= capacity) {
            running = false;
            error("AQUARIUM REACHED MAXIMUM CAPACITY! >> Check out the log files to see full logs.");
            System.exit(0);
        }
        fishList.add(fish);
        fish.start();
    }

    /**
     * Akvariumdan o'lgan baliqni olib tashlaydigan method
     * @param fish o'lgan baliq
     */
    public void removeFish(Fish fish) {
        fishList.remove(fish);
        if (fishList.isEmpty()) {
            running = false;
            error("ALL FISH DEAD! >> Check out the log files to see full logs.");
            System.exit(-1);
        }
    }

    /**
     * Simulyatsiyani boshlaydigan asosiy method.
     * Akvariumdagi default paydo bo'ladigan dastlabki baliqlar soni random aniqlanadi va simulyatsiyaga start beriladi
     */
    public void startSimulation() {
        int males = random.nextInt(10, 20);
        int females = random.nextInt(10, 20);
        log("Simulation started with %s MALES and %s FEMALES".formatted(males, females));

        for (int i = 0; i < males; i++) {
            Position pos = getRandomPosition();
            Fish maleFish = new Fish(Gender.MALE, pos, this);
            log("Fish (%s)%s born with lifespan %s seconds.".formatted(maleFish.getGender().getSymbol(), maleFish.getFID(), maleFish.getLifespan()));
            addFish(maleFish);
        }

        for (int i = 0; i < females; i++) {
            Position pos = getRandomPosition();
            Fish femaleFish = new Fish(Gender.FEMALE, pos, this);
            log("Fish (%s)%s born with lifespan %s seconds.".formatted(femaleFish.getGender().getSymbol(), femaleFish.getFID(), femaleFish.getLifespan()));
            addFish(femaleFish);
        }
        new ReproductionManager(this).start();
    }

    public Queue<Fish> getFishList() {
        return fishList;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isRunning() {
        return running;
    }

    public Position getRandomPosition() {
        int x = random.nextInt(getX());
        int y = random.nextInt(getY());
        int z = random.nextInt(getZ());
        return new Position(x, y, z);
    }
}
