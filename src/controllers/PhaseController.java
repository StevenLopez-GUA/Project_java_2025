package controllers;

import com.google.gson.reflect.TypeToken;
import model.Phase;
import model.Record;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PhaseController {
    private static final String PHASES_FILE = "phases.json";

    /** Lee todas las fases del archivo JSON */
    private List<Phase> getAll() {
        Type listType = new TypeToken<List<Phase>>(){}.getType();
        return JSONManager.readList(PHASES_FILE, listType);
    }

    /** Guarda la lista de fases en JSON */
    private void saveAll(List<Phase> list) {
        JSONManager.writeList(PHASES_FILE, list);
    }

    /** Obtiene el siguiente ID autoincrementable */
    private int getNextId() {
        int max = 0;
        for (Phase f : getAll()) {
            if (f.getPhaseId() > max) max = f.getPhaseId();
        }
        return max + 1;
    }

    /** Muestra todas las fases */
    public void showAll() {
        List<Phase> list = getAll();
        System.out.println("=== Lista de Fases ===");
        for (Phase f : list) {
            System.out.printf("ID:%d  Nombre:%s%n", f.getPhaseId(), f.getNamePhase());
        }
    }

    /** Muestra una fase por ID */
    public void showById(int id) {
        for (Phase f : getAll()) {
            if (f.getPhaseId() == id) {
                System.out.println("=== Phase Encontrada ===");
                System.out.printf("ID: %d%nNombre: %s%n", f.getPhaseId(), f.getNamePhase());
                return;
            }
        }
        System.out.println("No se encontró fase con ID " + id);
    }

    /** Agrega una nueva fase */
    public void add(Scanner sc) {
        int id = getNextId();
        System.out.println("Asignando ID: " + id);
        String name = InputValidator.readValidatedText(sc, "Nombre de la fase: ");
        Phase f = new Phase(id, name);
        List<Phase> list = getAll();
        list.add(f);
        saveAll(list);
        System.out.println("Phase agregada: " + name);
    }

    /** Actualiza una fase existente */
    public void update(Scanner sc) {
        int id = InputValidator.readValidatedInteger(sc, "ID de fase a actualizar: ");
        List<Phase> list = getAll();
        Phase existing = null;
        for (Phase f : list) {
            if (f.getPhaseId() == id) {
                existing = f;
                break;
            }
        }
        if (existing == null) {
            System.out.println("No se encontró fase con ID " + id);
            return;
        }
        System.out.println("Nombre actual: " + existing.getNamePhase());
        String newName = InputValidator.readValidatedText(sc, "Nuevo nombre de la fase: ");
        Phase updated = new Phase(id, newName);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPhaseId() == id) {
                list.set(i, updated);
                saveAll(list);
                System.out.println("Phase actualizada.");
                return;
            }
        }
    }

    /**
 * Elimina una fase solo si no está referenciada en el historial.
 */
public void delete(Scanner sc) {
    int id = InputValidator.readValidatedInteger(sc, "ID de fase a eliminar: ");

    // Verificar referencias en historial.json
    Type recordListType = new TypeToken<List<Record>>() {}.getType();
    List<Record> historial = JSONManager.readList("historial.json", recordListType);
    for (Record r : historial) {
        if (r.getPhaseId() == id) {
            System.out.println("No se puede eliminar: fase está usada en historial (Record ID: " 
                + r.getRecordId() + ").");
            return;
        }
    }

    // Si no hay referencias, proceder a eliminar
    List<Phase> list = getAll();
    for (Phase f : new ArrayList<>(list)) {
        if (f.getPhaseId() == id) {
            list.remove(f);
            saveAll(list);
            System.out.println("Fase eliminada: " + f.getNamePhase());
            return;
        }
    }
    System.out.println("No se encontró fase con ID " + id);
}

    /** Menú interactivo de CRUD de fases */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Gestión de Fases —");
            System.out.println("1. Ver todas");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Agregar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("6. Volver al menú principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (opt) {
                case 1 -> showAll();
                case 2 -> showById(InputValidator.readValidatedInteger(sc, "ID: "));
                case 3 -> add(sc);
                case 4 -> update(sc);
                case 5 -> delete(sc);
                case 6 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
            if (opt >= 1 && opt <= 5) {
                System.out.println("\nPresiona Enter para continuar..."); sc.nextLine();
            }
        } while (opt != 6);
    }
}
