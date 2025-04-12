import model.Computer;
import model.Record;
import model.Client;
import model.Phase;
import model.Technical;

import persistence.JSONManager;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String COMPUTER_FILE = "computers.json";
    private static final String RECORD_FILE = "records.json";

    // Ejemplo de un método para registrar computadoras
    public static void registerComputer(Computer comp) {
        Type listType = new TypeToken<List<Computer>>(){}.getType();
        List<Computer> list = JSONManager.readList(COMPUTER_FILE, listType);

        // Agregar la nueva computadora
        list.add(comp);

        // Guardar nuevamente la lista
        JSONManager.writeList(COMPUTER_FILE, list);

        System.out.println("Computadora registrada: " + comp.getServiceTag());
    }

    // Ejemplo para mostrar todas las computadoras
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
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Registrar Computadora");
            System.out.println("2. Ver Computadoras Registradas");
            System.out.println("3. Salir");
            System.out.print("Opción: ");
            option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 1:
                    System.out.println("Ingrese datos de la computadora:");
                    System.out.print("Service Tag: ");
                    String serviceTag = sc.nextLine();
                    System.out.print("ID de Cliente: ");
                    int clientId = Integer.parseInt(sc.nextLine());
                    System.out.print("Descripción del problema: ");
                    String problem = sc.nextLine();
                    System.out.print("Fecha de recepción (YYYY-MM-DD): ");
                    String receptionDate = sc.nextLine();

                    Computer comp = new Computer(serviceTag, clientId, problem, receptionDate);
                    registerComputer(comp);
                    break;

                case 2:
                    showComputers();
                    break;

                case 3:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida");
            }
        } while (option != 3);

        sc.close();
    }
}
