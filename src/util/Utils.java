package util;

public class Utils {

    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                // macOS / Linux / Unix: use 'clear'
                new ProcessBuilder("clear")
                        .inheritIO()
                        .start()
                        .waitFor();
            }
        } catch (Exception e) {
            // Fallback: print blank lines
            for (int i = 0; i < 5; i++) {
                System.out.println();
            }
        }
    }
}