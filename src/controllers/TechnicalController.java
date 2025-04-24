package controllers;

import com.google.gson.reflect.TypeToken;
import model.Technical;
import model.Record;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TechnicalController {
    private static final String TECHS_FILE = "technicians.json";

    /** Devuelve la lista completa de técnicos */
    private List<Technical> getAll() {
        Type listType = new TypeToken<List<Technical>>(){}.getType();
        return JSONManager.readList(TECHS_FILE, listType);
    }

    /** Guarda la lista completa de técnicos */
    private void saveAll(List<Technical> list) {
        JSONManager.writeList(TECHS_FILE, list);
    }

    /** Genera el siguiente ID autoincrementable */
    private int getNextId() {
        int max = 0;
        for (Technical t : getAll()) {
            if (t.getTechnicalId() > max) max = t.getTechnicalId();
        }
        return max + 1;
    }

    /** Muestra todos los técnicos */
    public void showAll() {
        List<Technical> list = getAll();
        System.out.println("=== Lista de Técnicos ===");
        for (Technical t : list) {
            System.out.printf("ID:%d  Nombre:%s%n", t.getTechnicalId(), t.getNameTechnical());
        }
    }

    /** Muestra un técnico por ID */
    public void showById(int id) {
        for (Technical t : getAll()) {
            if (t.getTechnicalId() == id) {
                System.out.println("=== Técnico Encontrado ===");
                System.out.printf("ID: %d%nNombre: %s%n", t.getTechnicalId(), t.getNameTechnical());
                return;
            }
        }
        System.out.println("No se encontró técnico con ID " + id);
    }

    /** Agrega un nuevo técnico */
    public void add(Scanner sc) {
        int id = getNextId();
        System.out.println("Asignando ID: " + id);
        String nombre = InputValidator.readValidatedText(sc, "Nombre del técnico: ");
        Technical t = new Technical(id, nombre);
        List<Technical> list = getAll();
        list.add(t);
        saveAll(list);
        System.out.println("Técnico agregado: " + nombre);
    }

    /** Actualiza un técnico existente */
    public void update(Scanner sc) {
        int id = InputValidator.readValidatedInteger(sc, "ID de técnico a actualizar: ");
        List<Technical> list = getAll();
        Technical existing = null;
        for (Technical t : list) {
            if (t.getTechnicalId() == id) {
                existing = t;
                break;
            }
        }
        if (existing == null) {
            System.out.println("No se encontró técnico con ID " + id);
            return;
        }
        System.out.println("Nombre actual: " + existing.getNameTechnical());
        String nuevoNombre = InputValidator.readValidatedText(sc, "Nuevo nombre: ");
        Technical updated = new Technical(id, nuevoNombre);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTechnicalId() == id) {
                list.set(i, updated);
                saveAll(list);
                System.out.println("Técnico actualizado.");
                return;
            }
        }
    }

    
/**
 * Elimina un técnico solo si no está referenciado en el historial.
 */
public void delete(Scanner sc) {
    int id = InputValidator.readValidatedInteger(sc, "ID de técnico a eliminar: ");

    // Verificar referencias en historial.json
    Type recordListType = new TypeToken<List<Record>>() {}.getType();
    List<Record> historial = JSONManager.readList("historial.json", recordListType);
    for (Record r : historial) {
        if (r.getTechnicalId() != null && r.getTechnicalId() == id) {
            System.out.println("No se puede eliminar: técnico está usado en historial (Record ID: " 
                + r.getRecordId() + ").");
            return;
        }
    }

    // Si no hay referencias, proceder a eliminar
    List<Technical> list = getAll();
    for (Technical t : new ArrayList<>(list)) {
        if (t.getTechnicalId() == id) {
            list.remove(t);
            saveAll(list);
            System.out.println("Técnico eliminado: " + t.getNameTechnical());
            return;
        }
    }
    System.out.println("No se encontró técnico con ID " + id);
}

    /** Menú interactivo de CRUD de técnicos */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Gestión de Técnicos —");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Agregar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("6. Volver");
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
