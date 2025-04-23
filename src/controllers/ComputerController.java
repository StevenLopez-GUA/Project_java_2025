package controllers;

import com.google.gson.reflect.TypeToken;
import model.Computer;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ComputerController {
    private static final String COMPUTERS_FILE = "computers.json";

    /** Lee todos los equipos del JSON */
    private List<Computer> getAll() {
        Type listType = new TypeToken<List<Computer>>() {}.getType();
        return JSONManager.readList(COMPUTERS_FILE, listType);
    }

    /** Guarda la lista de equipos en el JSON */
    private void saveAll(List<Computer> list) {
        JSONManager.writeList(COMPUTERS_FILE, list);
    }

    /** Muestra todos los equipos */
    public void showAll() {
        List<Computer> list = getAll();
        System.out.println("=== Lista de Computadoras ===");
        for (Computer c : list) {
            System.out.printf("ServiceTag:%s | ClienteID:%d | Problema:%s | Fecha:%s%n",
                    c.getServiceTag(), c.getClientId(), c.getProblemDescription(), c.getReceptionDate());
        }
    }

    /** Muestra un equipo por su serviceTag */
    public void showByTag(String tag) {
        for (Computer c : getAll()) {
            if (c.getServiceTag().equals(tag)) {
                System.out.println("=== Computadora Encontrada ===");
                System.out.printf("ServiceTag: %s%nClienteID: %d%nProblema: %s%nFecha: %s%n",
                        c.getServiceTag(), c.getClientId(), c.getProblemDescription(), c.getReceptionDate());
                return;
            }
        }
        System.out.println("No se encontró ninguna computadora con ServiceTag '" + tag + "'.");
    }

    /** Agrega una nueva computadora */
    public void add(Scanner sc) {
        String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag: ");
        int clientId = InputValidator.readValidatedInteger(sc, "ID de Cliente: ");
        String problem = InputValidator.readValidatedText(sc, "Descripción del problema: ");
        String date = InputValidator.readValidatedDate(sc, "Fecha de recepción (YYYY-MM-DD): ");

        Computer comp = new Computer(tag, clientId, problem, date);
        List<Computer> list = getAll();
        list.add(comp);
        saveAll(list);
        System.out.println("Computadora registrada: " + tag);
    }

    /** Actualiza una computadora existente */
    public void update(Scanner sc) {
        String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag de la computadora a actualizar: ");
        List<Computer> list = getAll();
        Computer existing = null;
        for (Computer c : list) {
            if (c.getServiceTag().equals(tag)) {
                existing = c;
                break;
            }
        }
        if (existing == null) {
            System.out.println("No se encontró la computadora con ServiceTag '" + tag + "'.");
            return;
        }
        // Mostrar datos actuales
        System.out.println("=== Datos Actuales ===");
        System.out.printf("1. ClienteID: %d%n2. Problema: %s%n3. Fecha: %s%n4. Todos los campos%n",
                existing.getClientId(), existing.getProblemDescription(), existing.getReceptionDate());
        int choice = InputValidator.readValidatedInteger(sc, "¿Qué campo deseas actualizar? ");

        int newClientId = existing.getClientId();
        String newProblem = existing.getProblemDescription();
        String newDate = existing.getReceptionDate();

        switch (choice) {
            case 1 -> newClientId = InputValidator.readValidatedInteger(sc, "Nuevo ID de Cliente: ");
            case 2 -> newProblem = InputValidator.readValidatedText(sc, "Nueva descripción del problema: ");
            case 3 -> newDate = InputValidator.readValidatedDate(sc, "Nueva fecha (YYYY-MM-DD): ");
            case 4 -> {
                newClientId = InputValidator.readValidatedInteger(sc, "Nuevo ID de Cliente: ");
                newProblem = InputValidator.readValidatedText(sc, "Nueva descripción del problema: ");
                newDate = InputValidator.readValidatedDate(sc, "Nueva fecha (YYYY-MM-DD): ");
            }
            default -> System.out.println("Opción inválida. No se realizaron cambios.");
        }
        // Reemplazar
        Computer updated = new Computer(tag, newClientId, newProblem, newDate);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getServiceTag().equals(tag)) {
                list.set(i, updated);
                saveAll(list);
                System.out.println("Computadora actualizada.");
                return;
            }
        }
    }

    /** Elimina una computadora por serviceTag */
    public void delete(Scanner sc) {
        String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag de la computadora a eliminar: ");
        List<Computer> list = getAll();
        for (Computer c : new ArrayList<>(list)) {
            if (c.getServiceTag().equals(tag)) {
                list.remove(c);
                saveAll(list);
                System.out.println("Computadora eliminada: " + tag);
                return;
            }
        }
        System.out.println("No se encontró la computadora con ServiceTag '" + tag + "'.");
    }

    /** Menú interactivo de CRUD para Computadoras */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Gestión de Computadoras —");
            System.out.println("1. Ver todas");
            System.out.println("2. Buscar por ServiceTag");
            System.out.println("3. Agregar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("6. Volver al menú principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (opt) {
                case 1 -> showAll();
                case 2 -> {
                    String tag = InputValidator.readValidatedAlphanumeric(sc, "ServiceTag: ");
                    showByTag(tag);
                }
                case 3 -> add(sc);
                case 4 -> update(sc);
                case 5 -> delete(sc);
                case 6 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
            if (opt >= 1 && opt <= 5) {
                System.out.println("\nPresiona Enter para continuar...");
                sc.nextLine();
            }
        } while (opt != 6);
    }
}
