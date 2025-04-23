import controllers.ClientController;
import logic.WarrantyManager;
import model.Computer;
import persistence.JSONManager;
import util.Utils;
import util.InputValidator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String COMPUTER_FILE = "computers.json";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientController clientCtrl = new ClientController();
        WarrantyManager warrantyMgr = new WarrantyManager();

        int option;
        do {
            Utils.clearConsole();
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Gestión de Computadoras");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Mover Computadora de Fase");
            System.out.println("4. Salir");
            option = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (option) {
                case 1 -> computerMenu(sc);
                case 2 -> {
                    Utils.clearConsole();
                    clientCtrl.menu(sc);
                }
                case 3 -> {
                    Utils.clearConsole();
                    moveComputerMenu(sc, warrantyMgr);
                }
                case 4 -> System.out.println("¡Hasta luego!");
                default -> {
                    System.out.println("Opción inválida. Presione Enter para continuar...");
                    sc.nextLine();
                }
            }
        } while (option != 4);

        sc.close();
    }

    /** Sub-menú para registrar y mostrar computadoras */
    private static void computerMenu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("--- Computadoras ---");
            System.out.println("1. Registrar Computadora");
            System.out.println("2. Ver Computadoras Registradas");
            System.out.println("3. Volver al Menú Principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (opt) {
                case 1 -> {
                    try {
                        System.out.println("Ingrese datos de la computadora:");
                        String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag: ");
                        int clientId = InputValidator.readValidatedInteger(sc, "ID de Cliente: ");
                        String problema = InputValidator.readValidatedText(sc, "Descripción del problema: ");
                        String fecha = InputValidator.readValidatedDate(sc, "Fecha de recepción (YYYY-MM-DD): ");

                        Computer comp = new Computer(tag, clientId, problema, fecha);
                        registerComputer(comp);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> showComputers();
                case 3 -> { /* vuelve */ }
                default -> System.out.println("Opción inválida.");
            }

            if (opt != 3) {
                System.out.println("Presione Enter para continuar...");
                sc.nextLine();
            }
        } while (opt != 3);
    }

    /** Menú para mover computadoras de fase */
    private static void moveComputerMenu(Scanner sc, WarrantyManager mgr) {
        try {
            System.out.println("Mover Computadora a otra fase:");
            String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag: ");
            int newPhase = InputValidator.readValidatedInteger(sc, "ID de la nueva fase: ");
            int techIdRaw = InputValidator.readValidatedInteger(sc, "ID de Técnico (0 si no aplica): ");
            Integer techId = (techIdRaw == 0 ? null : techIdRaw);
            String detalles = InputValidator.readValidatedText(sc, "Detalles del movimiento: ");

            mgr.moverComputadora(tag, newPhase, techId, detalles);
        } catch (Exception e) {
            System.out.println("Error al mover computadora: " + e.getMessage());
        }
        System.out.println("Presione Enter para continuar...");
        sc.nextLine();
    }

    /** Registra una computadora en el JSON */
    private static void registerComputer(Computer comp) {
        Type type = new TypeToken<List<Computer>>() {}.getType();
        List<Computer> list = JSONManager.readList(COMPUTER_FILE, type);
        list.add(comp);
        JSONManager.writeList(COMPUTER_FILE, list);
        System.out.println("Computadora registrada: " + comp.getServiceTag());
    }

    /** Muestra todas las computadoras registradas */
    private static void showComputers() {
        Type type = new TypeToken<List<Computer>>() {}.getType();
        List<Computer> list = JSONManager.readList(COMPUTER_FILE, type);
        System.out.println("=== Lista de Computadoras ===");
        for (Computer c : list) {
            System.out.printf("ServiceTag: %s | ClienteID: %d | Problema: %s | Fecha: %s%n",
                c.getServiceTag(), c.getClientId(), c.getProblemDescription(), c.getReceptionDate());
        }
    }
}
