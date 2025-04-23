package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {

    /**
     * Lee y valida una fecha con el formato YYYY-MM-DD.
     */
    public static String readValidatedDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Por favor, ingresa la fecha en formato YYYY-MM-DD.");
            }
        }
    }

    /**
     * Lee y valida un correo electrónico.
     */
    public static String readValidatedEmail(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return input;
            } else {
                System.out.println("Formato de correo inválido. Por favor ingresa un correo válido.");
            }
        }
    }

    /**
     * Lee y valida un número de teléfono de exactamente 8 dígitos.
     */
    public static String readValidatedPhone(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("\\d{8}")) {
                return input;
            } else {
                System.out.println("Número de teléfono inválido. Debe contener exactamente 8 dígitos.");
            }
        }
    }

    /**
     * Lee y valida un dato alfanumérico (sin espacios ni caracteres especiales).
     */
    public static String readValidatedAlphanumeric(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (input.matches("[A-Za-z0-9]+")) {
                return input;
            } else {
                System.out.println("Entrada inválida. Por favor, ingresa solo caracteres alfanuméricos (sin espacios ni símbolos).");
            }
        }
    }

    /**
     * Lee un número entero, verificándolo.
     */
    public static int readValidatedInteger(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingresa un número entero.");
            }
        }
    }

    /**
     * Lee una entrada de texto (puede incluir espacios) y verifica que no esté vacía.
     */
    public static String readValidatedText(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("La entrada no puede estar vacía. Inténtalo de nuevo.");
            }
        }
    }
}
