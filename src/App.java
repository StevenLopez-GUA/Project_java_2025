import controllers.ClientController;
import controllers.ComputerController;
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientController clientCtrl = new ClientController();
        ComputerController compCtrl = new ComputerController();
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
                case 1 -> {
                    Utils.clearConsole();
                    compCtrl.menu(sc);
                }
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
}
