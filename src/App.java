import controllers.ClientController;
import controllers.ComputerController;
import controllers.PhaseController;
import controllers.RecordController;
import controllers.StatisticsController;
import controllers.TechnicalController;
import logic.WarrantyManager;
import util.Utils;
import util.DataInitializer;
import util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        DataInitializer.init();

        Scanner sc = new Scanner(System.in);
        ClientController clientCtrl = new ClientController();
        ComputerController compCtrl = new ComputerController();
        TechnicalController techCtrl = new TechnicalController();
        WarrantyManager warrantyMgr = new WarrantyManager();
        PhaseController phaseCtrl = new PhaseController();
        RecordController recordCtrl = new RecordController();
        StatisticsController statsCtrl = new StatisticsController();

        int option;
        do {
            Utils.clearConsole();
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Gestión de Computadoras");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Gestión de Técnicos");
            System.out.println("4. Gestión de Fases");
            System.out.println("5. Ver Historial de Garantías");
            System.out.println("6. Mover Computadora de Fase");
            System.out.println("7. Procesar siguiente en cola de fase");
            System.out.println("8. Ver cola de espera de fase");
            System.out.println("9. Informes Estadísticos");
            System.out.println("0. Salir");
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
                    techCtrl.menu(sc);
                }
                case 4 -> {
                    Utils.clearConsole();
                    phaseCtrl.menu(sc);
                }
                case 5 -> {
                    Utils.clearConsole();
                    recordCtrl.menu(sc);
                }
                case 6 -> {
                    Utils.clearConsole();
                    moveComputerMenu(sc, warrantyMgr);
                }
                case 7 -> {
                    Utils.clearConsole();
                    int fase = InputValidator.readValidatedInteger(sc,
                            "¿De qué fase quieres procesar la cola? (1-Recep,2-Ins,3-Rep,4-C.C,5-Ent): ");
                    warrantyMgr.processNextInPhase(fase, sc);
                    System.out.println("\nPresiona Enter para continuar...");
                    sc.nextLine();
                }
                case 8 -> {
                    Utils.clearConsole();
                    System.out.println("— Cola de espera de fase —");
                    int phaseId = InputValidator.readValidatedInteger(sc,
                        "¿De qué fase quieres ver la cola? (1-Recepción,2-Inspección,3-Reparación,4-Control calidad,5-Entrega): ");
                    List<String> queue = warrantyMgr.getQueue(phaseId);
                    if (queue.isEmpty()) {
                        System.out.println("No hay computadoras en espera para la fase " + phaseId + ".");
                    } else {
                        System.out.println("Computadoras en cola para fase " + phaseId + ":");
                        for (String qtag : queue) {
                            System.out.println(" - " + qtag);
                        }
                    }
                    System.out.println("\nPresione Enter para continuar..."); sc.nextLine();
                }
                case 9 -> {
                    Utils.clearConsole();
                    statsCtrl.menu(sc);
                }
                case 0 -> System.out.println("¡Hasta luego!");
                default -> {
                    System.out.println("Opción inválida. Presione Enter para continuar...");
                    sc.nextLine();
                }
            }
        } while (option != 0);

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
