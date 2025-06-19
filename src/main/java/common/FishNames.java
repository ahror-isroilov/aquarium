package common;

import model.Gender;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Baliqlarning FIDga qo'shiladigan random 'petname'larni olish uchun mo'ljallangan helper class
 */
public class FishNames {
    private static final String NAMES_FILE = "names.yml";
    private static final Random random = new Random();
    private static Map<String, List<String>> namesData;

    static {
        loadNames();
    }

    private static void loadNames() {
        try {
            InputStream inputStream = FishNames.class.getClassLoader()
                    .getResourceAsStream(NAMES_FILE);

            if (inputStream == null) {
                throw new RuntimeException("Could not find " + NAMES_FILE + " in classpath");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            namesData = new HashMap<>();
            String line;
            String currentGender;
            List<String> currentNames = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (!line.startsWith(" ") && !line.startsWith("-") && line.endsWith(":")) {
                    currentGender = line.substring(0, line.length() - 1).trim();
                    currentNames = new ArrayList<>();
                    namesData.put(currentGender, currentNames);
                } else if (line.startsWith("-") && currentNames != null) {
                    String name = line.substring(1).trim();
                    if (!name.isEmpty()) {
                        currentNames.add(name);
                    }
                }
            }

            reader.close();
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Error loading names from YAML file", e);
        }
    }

    public static String randomName(Gender gender) {
        List<String> names = namesData.get(gender.equals(Gender.MALE) ? "male" : "female");
        int randomIndex = random.nextInt(names.size());
        return names.get(randomIndex);
    }
}
