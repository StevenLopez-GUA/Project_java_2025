package controllers;

import com.google.gson.reflect.TypeToken;

import model.Phase;
import model.Record;
import model.Technical;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class RecordController {
    private static final String HISTORIAL_FILE = "historial.json";
    private static final String PHASES_FILE = "phases.json";
    private static final String TECHS_FILE = "technicians.json";

    /** Lee todos los registros de historial desde el JSON */
    private List<Record> getAll() {
        Type listType = new TypeToken<List<Record>>() {
        }.getType();
        return JSONManager.readList(HISTORIAL_FILE, listType);
    }

    private String getPhasesName(int phaseID) {
        Type listType = new TypeToken<List<Phase>>() {
        }.getType();
        List<Phase> phases = JSONManager.readList(PHASES_FILE, listType);
        for (Phase ph : phases) {
            if (ph.getPhaseId() == phaseID) {
                return ph.getNamePhase();
            }
        }
        return "Desconocida";
    }

    private String getTechnicalName(int technicalId) {
        Type listType = new TypeToken<List<Technical>>() {
        }.getType();
        List<Technical> technicals = JSONManager.readList(TECHS_FILE, listType);
        for (Technical tc : technicals) {
            if (tc.getTechnicalId() == technicalId) {
                return tc.getNameTechnical();
            }
        }
        return "Desconocido";
    }

    /** Muestra todos los registros de historial */
    public void showAll() {
        List<Record> list = getAll();
        System.out.println("=== Historial Completo ===");
        for (Record r : list) {
            String namePhase = getPhasesName(r.getPhaseId());
            String nameTechnical = (r.getTechnicalId() == null) ? "N/A" : getTechnicalName(r.getTechnicalId());
            System.out.printf("ID:%d | Tag:%s | Fase:%s | Técnico:%s | Entrada:%s | Salida:%s | Detalles:%s%n",
                    r.getRecordId(),
                    r.getServiceTag(),
                    namePhase,
                    // (r.getTechnicalId() == null ? "N/A" : r.getTechnicalId()),
                    nameTechnical,
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
                String namePhase = getPhasesName(r.getPhaseId());
                String nameTechnical = (r.getTechnicalId() == null) ? "N/A" : getTechnicalName(r.getTechnicalId());
                System.out.printf("ID:%d | Fase:%s | Técnico:%s | Entrada:%s | Salida:%s | Detalles:%s%n",
                        r.getRecordId(),
                        namePhase,
                        nameTechnical,
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
            System.out.println("0. Volver al menú principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");
            switch (opt) {
                case 1 -> showAll();
                case 2 -> showByTag(sc);
                case 0 -> {
                    System.out.println("Volviendo al menú principal...");
                    Utils.clearConsole();
                }
                default -> System.out.println("Opción inválida.");
            }
            if (opt == 1 || opt == 2) {
                System.out.println("\nPresiona Enter para continuar...");
                sc.nextLine();
                Utils.clearConsole();
            }
        } while (opt != 0);
    }
}
