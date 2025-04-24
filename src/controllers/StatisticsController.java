package controllers;

import com.google.gson.reflect.TypeToken;
import model.Record;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsController {
    private static final String HISTORIAL_FILE = "historial.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** Menú interactivo de informes */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Informes Estadísticos —");
            System.out.println("1. Computadoras en cada fase");
            System.out.println("2. Tiempo promedio por fase");
            System.out.println("3. Carga de trabajo por técnico");
            System.out.println("4. Volver al menú principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");
            Utils.clearConsole();
            switch (opt) {
                case 1 -> reportCountsByPhase();
                case 2 -> reportAvgTimeByPhase();
                case 3 -> reportWorkloadByTech();
                case 4 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
            if (opt >= 1 && opt <= 3) {
                System.out.println("\nPresiona Enter para continuar...");
                sc.nextLine();
            }
        } while (opt != 4);
    }

    /** Carga todos los registros de historial */
    private List<Record> loadRecords() {
        Type listType = new TypeToken<List<Record>>() {
        }.getType();
        return JSONManager.readList(HISTORIAL_FILE, listType);
    }

    /** Informe: número de computadoras pendientes en cada fase */
    private void reportCountsByPhase() {
        Map<Integer, Long> counts = new HashMap<>();
        for (Record r : loadRecords()) {
            if (r.getDepartureDate() == null) {
                counts.merge(r.getPhaseId(), 1L, Long::sum);
            }
        }
        System.out.println("=== Computadoras pendientes por fase ===");
        counts.forEach((phase, cnt) -> System.out.printf("Fase %d: %d%n", phase, cnt));
    }

    /** Informe: tiempo promedio (en minutos) que tardan en cerrar cada fase */
    private void reportAvgTimeByPhase() {
        Map<Integer, List<Duration>> durations = new HashMap<>();
        for (Record r : loadRecords()) {
            if (r.getDepartureDate() != null) {
                LocalDateTime start = LocalDateTime.parse(r.getEntryDate(), FORMATTER);
                LocalDateTime end = LocalDateTime.parse(r.getDepartureDate(), FORMATTER);
                Duration d = Duration.between(start, end);
                durations.computeIfAbsent(r.getPhaseId(), k -> new ArrayList<>()).add(d);
            }
        }
        System.out.println("=== Tiempo promedio por fase (minutos) ===");
        durations.forEach((phase, list) -> {
            double avg = list.stream()
                    .mapToLong(Duration::toMinutes)
                    .average()
                    .orElse(0.0);
            System.out.printf("Fase %d: %.2f%n", phase, avg);
        });
    }

    /** Informe: número de intervenciones por cada técnico */
    private void reportWorkloadByTech() {
        Map<Integer, Long> workload = new HashMap<>();
        for (Record r : loadRecords()) {
            if (r.getTechnicalId() != null) {
                workload.merge(r.getTechnicalId(), 1L, Long::sum);
            }
        }
        System.out.println("=== Carga de trabajo por técnico ===");
        workload.forEach((tech, cnt) -> System.out.printf("Técnico %d: %d tareas%n", tech, cnt));
    }
}
