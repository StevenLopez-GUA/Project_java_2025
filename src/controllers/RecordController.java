package controllers;

import com.google.gson.reflect.TypeToken;
import model.Record;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class RecordController {
    private static final String HISTORIAL_FILE = "historial.json";

    /** Lee todos los registros de historial desde el JSON */
    private List<Record> getAll() {
        Type listType = new TypeToken<List<Record>>() {
        }.getType();
        return JSONManager.readList(HISTORIAL_FILE, listType);
    }

    /** Muestra todos los registros de historial */
    public void showAll() {
        List<Record> list = getAll();
        System.out.println("=== Historial Completo ===");
        for (Record r : list) {
            System.out.printf("ID:%d | Tag:%s | Fase:%d | Técnico:%s | Entrada:%s | Salida:%s | Detalles:%s%n",
                    r.getRecordId(),
                    r.getServiceTag(),
                    r.getPhaseId(),
                    (r.getTechnicalId() == null ? "N/A" : r.getTechnicalId()),
                    r.getEntryDate(),
                    (r.getDepartureDate() == null ? "En curso" : r.getDepartureDate()),
                    r.getDetails());
        }
    }

    /** Muestra el historial de una computadora, filtrando por serviceTag */
    public void showByTag(Scanner sc) {
        String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag a consultar: ");
        List<Record> list = getAll();
        System.out.println("=== Historial de " + tag + " ===");
        boolean found = false;
        for (Record r : list) {
            if (r.getServiceTag().equals(tag)) {
                found = true;
                System.out.printf("ID:%d | Fase:%d | Técnico:%s | Entrada:%s | Salida:%s | Detalles:%s%n",
                        r.getRecordId(),
                        r.getPhaseId(),
                        (r.getTechnicalId() == null ? "N/A" : r.getTechnicalId()),
                        r.getEntryDate(),
                        (r.getDepartureDate() == null ? "En curso" : r.getDepartureDate()),
                        r.getDetails());
            }
        }
        if (!found) {
            System.out.println("No hay registros para el Service Tag proporcionado.");
        }
    }

    /** Menú interactivo de historial */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Gestión de Historial —");
            System.out.println("1. Ver historial completo");
            System.out.println("2. Ver historial por Service Tag");
            System.out.println("3. Volver al menú principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");
            Utils.clearConsole();
            switch (opt) {
                case 1 -> showAll();
                case 2 -> showByTag(sc);
                case 3 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
            if (opt == 1 || opt == 2) {
                System.out.println("\nPresiona Enter para continuar...");
                sc.nextLine();
            }
        } while (opt != 3);
    }
}
