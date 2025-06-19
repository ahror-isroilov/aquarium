package common;

import model.Gender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

public final class Utils {
    public static final Random random = new Random();

    public static int randomDelta() {
        return random.nextInt(3) - 1;
    }

    public static int checkBounds(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static String genId(Gender gender) {
        return FishNames.randomName(gender) + "#" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static void clearLogs() {
        try {
            Files.delete(Paths.get("movements.log"));
        } catch (IOException ignored) {
        }
        try {
            Files.delete(Paths.get("lineages.log"));
        } catch (IOException ignored) {
        }
        try {
            Files.delete(Paths.get("reproductions.log"));
        } catch (IOException ignored) {
        }
    }
}
