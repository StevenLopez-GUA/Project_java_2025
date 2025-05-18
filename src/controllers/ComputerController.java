package controllers;

import com.google.gson.reflect.TypeToken;

import auth.AuthenticationService;
import logic.WarrantyManager;
import model.Computer;
import model.Client;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class ComputerController {
    private static final String COMPUTERS_FILE = "computers.json";
    private static final String CLIENTS_FILE = "clients.json";

    private final WarrantyManager warrantyMgr;

    // Dentro de ComputerController.java
    private AuthenticationService auth;

    public ComputerController(AuthenticationService auth) {
        this.auth = auth;
        this.warrantyMgr = new WarrantyManager(auth);
    }

    /** Recupera el nombre de cliente dado su ID */
    private String getClientName(int clientId) {
        Type listType = new TypeToken<List<Client>>() {
        }.getType();
        List<Client> clients = JSONManager.readList(CLIENTS_FILE, listType);
        for (Client cl : clients) {
            if (cl.getClientId() == clientId) {
                return cl.getName(); // Ajusta el getter según tu modelo Client
            }
        }
        return "Desconocido";
    }

    /** Lee todos los equipos del JSON */
    private List<Computer> getAll() {
        Type listType = new TypeToken<List<Computer>>() {
        }.getType();
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
            String nombreCliente = getClientName(c.getClientId());
            System.out.printf("ServiceTag:%s | Cliente:%s | Problema:%s | Fecha:%s%n",
                    c.getServiceTag(), nombreCliente, c.getProblemDescription(), c.getReceptionDate());
        }
    }

    /** Muestra un equipo por su serviceTag */
    public void showByTag(String tag) {
        for (Computer c : getAll()) {
            if (c.getServiceTag().equals(tag)) {
                System.out.println("=== Computadora Encontrada ===");
                String nombreCliente = getClientName(c.getClientId());
                System.out.printf("ServiceTag: %s%nCliente: %s%nProblema: %s%nFecha: %s%n",
                        c.getServiceTag(), nombreCliente, c.getProblemDescription(), c.getReceptionDate());
                return;
            }
        }
        System.out.println("No se encontró ninguna computadora con ServiceTag '" + tag + "'.");

    }

    /** Agrega una nueva computadora y crea registro inicial de recepción */
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

        // Encolamos en fase inicial (Recepción)
        warrantyMgr.enqueueInitialPhase(tag, clientId);
        System.out.println("Computadora " + tag + " encolada en fase Recepción.");
    }

    /** Actualiza una computadora existente */
    public void update(Scanner sc) {

        showAll();
        System.out.println();

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
        // 1) Listar todos los registros
        showAll();
        System.out.println();

        // 2) Pedir ServiceTag a eliminar
        String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag de la computadora a eliminar: ");

        // 3) Confirmar eliminación
        String confirm;
        do {
            System.out.print("¿Confirmas eliminación de '" + tag + "'? (s/n): ");
            confirm = sc.nextLine().trim().toLowerCase();
        } while (!confirm.equals("s") && !confirm.equals("n"));

        if (confirm.equals("n")) {
            System.out.println("Operación cancelada. No se eliminó ninguna computadora.");
            return;
        }

        // 4) Realizar eliminación
        List<Computer> list = getAll();
        boolean removed = list.removeIf(c -> c.getServiceTag().equals(tag));
        if (removed) {
            saveAll(list);
            System.out.println("Computadora eliminada: " + tag);
        } else {
            System.out.println("No se encontró la computadora con ServiceTag '" + tag + "'.");
        }
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
            System.out.println("0. Volver al menú principal");
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
                case 0 -> {
                    System.out.println("Volviendo al menú principal...");
                    Utils.clearConsole();
                }
                default -> System.out.println("Opción inválida.");
            }
            if (opt >= 1 && opt <= 5) {
                System.out.println("\nPresiona Enter para continuar...");
                sc.nextLine();
                Utils.clearConsole();
            }
        } while (opt != 0);
    }
}
