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

    public static void registerComputer(Computer comp) {
        Type listType = new TypeToken<List<Computer>>(){}.getType();
        List<Computer> list = JSONManager.readList(COMPUTER_FILE, listType);
        list.add(comp);
        JSONManager.writeList(COMPUTER_FILE, list);
        System.out.println("Computadora registrada: " + comp.getServiceTag());
    }

    public static void showComputers() {
        Type listType = new TypeToken<List<Computer>>(){}.getType();
        List<Computer> list = JSONManager.readList(COMPUTER_FILE, listType);
        System.out.println("=== Lista de Computadoras ===");
        for (Computer c : list) {
            System.out.println(c.getServiceTag() + " - " + c.getProblemDescription());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        WarrantyManager gestor = new WarrantyManager();
        int option;
        do {
            Utils.clearConsole();
            System.out.println("--- Menú Principal ---");
            System.out.println("1. Registrar Computadora");
            System.out.println("2. Ver Computadoras Registradas");
            System.out.println("3. Mover Computadora a otra Fase");
            System.out.println("4. Salir");
            
            option = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (option) {
                case 1:
                    try {
                        System.out.println("Ingrese datos de la computadora:");
                        String serviceTag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag: ");
                        int clientId = InputValidator.readValidatedInteger(sc, "ID de Cliente: ");
                        String problem = InputValidator.readValidatedText(sc, "Descripción del problema: ");
                        String receptionDate = InputValidator.readValidatedDate(sc, "Fecha de recepción (YYYY-MM-DD): ");

                        Computer comp = new Computer(serviceTag, clientId, problem, receptionDate);
                        registerComputer(comp);

                        System.out.println("Presione Enter para continuar...");
                        sc.nextLine();
                    } catch (Exception e) {
                        System.out.println("Se produjo un error: " + e.getMessage());
                        System.out.println("Inténtalo de nuevo. Presione Enter para continuar...");
                        sc.nextLine();
                    }
                    break;
                case 2:
                    try {
                        showComputers();
                        System.out.println("Presione Enter para continuar...");
                        sc.nextLine();
                    } catch (Exception e) {
                        System.out.println("Se produjo un error: " + e.getMessage());
                        System.out.println("Inténtalo de nuevo. Presione Enter para continuar...");
                        sc.nextLine();
                    }
                    break;
                case 3:
                    try {
                        // Solicitar datos para mover la computadora
                        System.out.println("Mover Computadora a otra fase:");
                        String moveTag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag de la Computadora: ");
                        int nuevaPhase = InputValidator.readValidatedInteger(sc, "ID de la nueva fase: ");
                        // Si es necesario solicitar el técnico, se puede usar un entero. Si no, se puede dejar null.
                        int techOption = InputValidator.readValidatedInteger(sc, "Ingrese ID de Técnico (0 si no aplica): ");
                        Integer technicalId = (techOption == 0) ? null : techOption;
                        String moveDetails = InputValidator.readValidatedText(sc, "Detalles del movimiento: ");

                        gestor.moverComputadora(moveTag, nuevaPhase, technicalId, moveDetails);
                        System.out.println("Presione Enter para continuar...");
                        sc.nextLine();
                    } catch (Exception e) {
                        System.out.println("Se produjo un error: " + e.getMessage());
                        System.out.println("Inténtalo de nuevo. Presione Enter para continuar...");
                        sc.nextLine();
                    }
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Presione Enter para continuar...");
                    sc.nextLine();
            }
        } while (option != 4);
        sc.close();
    }
}
