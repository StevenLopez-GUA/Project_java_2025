import auth.AuthenticationService;
import controllers.*;
import logic.WarrantyManager;
import util.*;

import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        DataInitializer.init(); // crea archivos JSON vacíos si no existen
        Scanner sc = new Scanner(System.in);

        /* Servicio de autenticación */
        AuthenticationService auth = new AuthenticationService();
        TechnicalController techCtrl = new TechnicalController();
        techCtrl.ensureAdmin();

        while (true) {
            while (!auth.isLoggedIn()) {
                Utils.clearConsole();
                System.out.println("=== Iniciar Sesión ===");
                doLogin(sc, auth);
                if (!auth.isLoggedIn()) {
                    System.out.println("Credenciales inválidas. Presiona Enter para reintentar.");
                    sc.nextLine();
                    Utils.clearConsole();
                }
            }
            Utils.clearConsole();

            /* Controladores (inyectamos auth al que lo ocupa) */
            ClientController clientCtrl = new ClientController();
            ComputerController compCtrl = new ComputerController(auth);
            PhaseController phaseCtrl = new PhaseController();
            RecordController recordCtrl = new RecordController();
            StatisticsController statsCtrl = new StatisticsController();
            WarrantyManager warrantyMgr = new WarrantyManager(auth);

            int option;

            do {
                Utils.clearConsole();

                /* ——— Encabezado de sesión ——— */
                if (auth.isLoggedIn()) {
                    System.out.println("Sesión: "
                            + auth.currentUser().getNameTechnical()
                            + "  (ID " + auth.currentUser().getTechnicalId() + ")");
                } else {
                    System.out.println("[Sin sesión]");
                }

                /* ——— Menú principal ——— */
                System.out.println("\n=== Menú Principal ===");
                System.out.println("1. Gestión de Computadoras");
                System.out.println("2. Gestión de Clientes");
                System.out.println("3. Gestión de Técnicos");
                System.out.println("4. Gestión de Fases");
                System.out.println("5. Ver Historial de Garantías");
                System.out.println("6. Mover Computadora de Fase");
                System.out.println("7. Procesar siguiente en cola de fase");
                System.out.println("8. Ver cola de espera de fase");
                System.out.println("9. Informes Estadísticos");
                System.out.println("0. Cerrar sesión");
                option = InputValidator.readValidatedInteger(sc, "Opción: ");

                switch (option) {
                    case 1 -> { // — Gestión de computadoras
                        if (!auth.isLoggedIn()) {
                            System.out.println("\nDebes iniciar sesión para usar esta sección.");
                            pause(sc);
                            break;
                        }
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
                        moveComputerMenu(sc, warrantyMgr, auth);
                    }
                    case 7 -> {
                        Utils.clearConsole();
                        int fase = InputValidator.readValidatedInteger(sc,
                                "¿De qué fase quieres procesar la cola? (1-Recep…5-Ent): ");
                        warrantyMgr.processNextInPhase(fase, sc);
                        pause(sc);
                    }
                    case 8 -> {
                        Utils.clearConsole();
                        int phaseId = InputValidator.readValidatedInteger(sc,
                                "¿De qué fase quieres ver la cola? (1-5): ");
                        List<String> queue = warrantyMgr.getQueue(phaseId);
                        if (queue.isEmpty())
                            System.out.println("No hay computadoras en espera.");
                        else
                            queue.forEach(tag -> System.out.println(" - " + tag));
                        pause(sc);
                    }
                    case 9 -> {
                        Utils.clearConsole();
                        statsCtrl.menu(sc);
                    }
                    case 0 -> {
                        auth.logout();
                        System.out.println("Sesión cerrada. Presiona Enter para continuar...");
                        sc.nextLine();
                    }
                    default -> {
                        System.out.println("Opción inválida.");
                        pause(sc);
                    }
                }
            } while (option != 0);
            // al cerrar sesión, vuelve al inicio del bucle
            continue;
        }

        // sc.close();
    }

    /* ---------- Helpers ---------- */

    private static void doLogin(Scanner sc, AuthenticationService auth) {
        String email = InputValidator.readValidatedEmail(sc, "Email: ");
        String pass = InputValidator.readPassword(sc, "Contraseña: ");
        if (auth.login(email, pass)) {
            System.out.println("Bienvenido " + auth.currentUser().getNameTechnical());
        } else {
            System.out.println("Credenciales inválidas");
        }
    }

    private static void pause(Scanner sc) {
        System.out.println("\nPresiona Enter para continuar…");
        sc.nextLine();
    }

    private static void moveComputerMenu(Scanner sc,
            WarrantyManager mgr,
            AuthenticationService auth) {
        try {
            System.out.println("Mover Computadora:");
            String tag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag: ");
            int newPhase = InputValidator.readValidatedInteger(sc, "ID de la nueva fase: ");

            /* Si hay sesión, sugerimos usar ese técnico; 0 para ninguno */
            Integer techId = null;
            if (auth.isLoggedIn()) {
                System.out.printf("Tec asignado (%d) Enter=usar / 0=otro: ",
                        auth.currentUser().getTechnicalId());
                String raw = sc.nextLine().trim();
                if (!raw.isBlank() && !raw.equals("0"))
                    techId = Integer.parseInt(raw);
                else if (raw.isBlank())
                    techId = auth.currentUser().getTechnicalId();
            } else {
                int techRaw = InputValidator.readValidatedInteger(sc,
                        "ID de Técnico (0 si no aplica): ");
                techId = (techRaw == 0 ? null : techRaw);
            }

            String detalles = InputValidator.readValidatedText(sc, "Detalles: ");
            mgr.moverComputadora(tag, newPhase, techId, detalles);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
