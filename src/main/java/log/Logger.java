package log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simulyatsiya davomidagi harakatlar va avlodlar listini tegishli log fayllarga shakllantiradi
 */
public final class Logger {
    private static final String red = "\u001B[31m";
    private static final String orange = "\u001B[38;5;214m";
    private static final String green = "\u001B[32m";
    private static final String blue = "\u001B[34m";
    private static final String reset = "\u001B[0m";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss:SSSS");

    public static void log(String log) {
        System.out.printf("[" + green + "INFO" + blue + " %s" + reset + "] %s%n", formatter.format(LocalDateTime.now()), log);
    }

    public static void log(String log, String filename) {
        try (FileWriter fw = new FileWriter(filename, true); PrintWriter pw = new PrintWriter(fw)) {
            pw.printf("[INFO %s] %s".formatted(formatter.format(LocalDateTime.now()), log));
            pw.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logHierarchy(String log, String filename) {
        try (FileWriter fw = new FileWriter(filename, true); PrintWriter pw = new PrintWriter(fw)) {
            pw.printf(log);
            pw.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void warn(String log) {
        System.out.printf("[" + orange + "WARN" + blue + " %s" + reset + "]" + orange + " %s%n" + reset, formatter.format(LocalDateTime.now()), log);
    }

    public static void error(String log) {
        System.out.printf("[" + red + "ERROR" + blue + " %s" + reset + "]" + red + " %s%n" + reset, formatter.format(LocalDateTime.now()), log);
    }
}
