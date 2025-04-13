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
        int option;
        do {
            // Limpia la consola utilizando Utils
            Utils.clearConsole();
            System.out.println("--- Menú Principal ---");
            System.out.println("1. Registrar Computadora");
            System.out.println("2. Ver Computadoras Registradas");
            System.out.println("3. Salir");
            // Utiliza el método para leer un entero validado
            option = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (option) {
                case 1:
                    try {
                        System.out.println("Ingrese datos de la computadora:");
                        // Para el service tag, si requieres que sea alfanumérico sin espacios:
                        String serviceTag = InputValidator.readValidatedAlphanumeric(sc, "Service Tag: ");
                        int clientId = InputValidator.readValidatedInteger(sc, "ID de Cliente: ");
                        // Lee la descripción como un texto (puede incluir espacios)
                        String problem = InputValidator.readValidatedText(sc, "Descripción del problema: ");
                        // Lee la fecha validada
                        String receptionDate = InputValidator.readValidatedDate(sc, "Fecha de recepción (YYYY-MM-DD): ");

                        Computer comp = new Computer(serviceTag, clientId, problem, receptionDate);
                        registerComputer(comp);
                        System.out.println("Presione Enter para continuar...");
                        sc.nextLine();
                    } catch (Exception e) {
                        System.out.println("Se produjo un error: " + e.getMessage());
                        System.out.println("Por favor, inténtalo de nuevo. Presiona Enter para continuar...");
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
                        System.out.println("Por favor, inténtalo de nuevo. Presione Enter para continuar...");
                        sc.nextLine();
                    }
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida. Presione Enter para continuar...");
                    sc.nextLine();
            }
        } while (option != 3);

        sc.close();
    }
}
