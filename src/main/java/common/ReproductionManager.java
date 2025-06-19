package common;

import model.Aquarium;
import model.Fish;
import model.Gender;
import model.Position;

import java.util.*;
import java.util.stream.Collectors;

import static common.Utils.random;
import static log.Logger.*;

/**
 * Akvariumdagi baliqlarning ko'payish jarayonini boshqaradi.
 * Erkak va ayol baliq bir koordinatada uchrashsa yangi baliq paydo bo'ladi
 */
public class ReproductionManager extends Thread {
    private final Aquarium aquarium;

    public ReproductionManager(Aquarium aquarium) {
        this.aquarium = aquarium;
    }

    @Override
    public void run() {
        while (aquarium.isRunning()) {
            Map<Integer, List<Fish>> groups = new HashMap<>();
            Queue<Fish> fishList = aquarium.getFishList();

            checkReproductionConditions(fishList);
            for (Fish fish : fishList) {
                Position currentPos = fish.getPosition();
                int key = currentPos.hashCode();
                groups.computeIfAbsent(key, pos -> new ArrayList<>()).add(fish);
            }

            for (Map.Entry<Integer, List<Fish>> entry : groups.entrySet()) {
                List<Fish> group = entry.getValue();
                if (group.size() < 2) continue;

                Fish male = group.stream().filter(f -> f.getGender() == Gender.MALE).findFirst().orElse(null);
                Fish female = group.stream().filter(f -> f.getGender() == Gender.FEMALE).findFirst().orElse(null);

                if (male != null && female != null) {
                    Gender gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
                    Fish fish = new Fish(gender, male.getPosition(), aquarium, male, female);
                    logHierarchy(fish.getLineage(), "lineages.log");
                    aquarium.addFish(fish);
                    log("🐟(%s-%s) born with %s seconds of lifespan. [All: %s]".formatted(fish.getGender().getSymbol(), fish.getFID(), fish.getLifespan(), fishList.size()));
                    log("🐟(%s-%s) born with %s seconds of lifespan. [All: %s]".formatted(fish.getGender().getSymbol(), fish.getFID(), fish.getLifespan(), fishList.size()), "reproductions.log");
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Baliqlar ko'payishi uchun zarur shartlarni tekshiradi.
     * @param fishList Tekshirilishi kerak bo'lgan baliqlar ro'yxati
     */
    private void checkReproductionConditions(Queue<Fish> fishList) {
        Set<Gender> genders = fishList.stream().map(Fish::getGender).collect(Collectors.toSet());
        if (!genders.containsAll(List.of(Gender.MALE, Gender.FEMALE))) {
            error("NO REPRODUCTION CONDITIONS DETECTED. ALL FISH CLASSIFIED AS A SINGLE GENDER. >> Check out the log files to see full logs.");
            System.exit(-1);
        }
    }
}
