package controllers;

import model.Client;
import model.Computer;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientController {
    private static final String CLIENTS_FILE = "clients.json";

    /** Lee todos los clientes desde el JSON */
    public List<Client> getAll() {
        Type listType = new TypeToken<List<Client>>() {
        }.getType();
        return JSONManager.readList(CLIENTS_FILE, listType);
    }

    /** Guarda la lista de clientes en el JSON */
    private void saveAll(List<Client> list) {
        JSONManager.writeList(CLIENTS_FILE, list);
    }

    /** Agrega un cliente nuevo */
    public void add(Client c) {
        List<Client> list = getAll();
        list.add(c);
        saveAll(list);
        System.out.println("Cliente agregado: " + c.getName());
    }

    /** Actualiza un cliente existente, busca por clientId */
    public boolean updateClient(Client updated) {
        List<Client> list = getAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClientId() == updated.getClientId()) {
                list.set(i, updated);
                saveAll(list);
                System.out.println("Cliente actualizado: " + updated.getName());
                return true;
            }
        }
        System.out.println("No se encontró cliente con ID " + updated.getClientId());
        return false;
    }

    /** Elimina un cliente por su ID, sólo si no tiene computadoras asociadas */
    public boolean delete(int clientId) {
        // 1) Leer todas las computadoras
        Type compListType = new TypeToken<List<Computer>>() {
        }.getType();
        List<Computer> comps = JSONManager.readList("computers.json", compListType);

        // 2) Verificar asociación
        for (Computer c : comps) {
            if (c.getClientId() == clientId) {
                System.out.println("No se puede eliminar: el cliente tiene computadoras asociadas (ServiceTag: "
                        + c.getServiceTag() + ").");
                return false;
            }
        }

        // 3) Si no hay asociaciones, proceder a borrar
        List<Client> list = getAll();
        for (Client c : new ArrayList<>(list)) {
            if (c.getClientId() == clientId) {
                list.remove(c);
                saveAll(list);
                System.out.println("Cliente eliminado: " + c.getName());
                return true;
            }
        }
        System.out.println("No se encontró cliente con ID " + clientId);
        return false;
    }

    /** Muestra todos los clientes */
    public void showAll() {
        List<Client> list = getAll();
        System.out.println("=== Lista de Clientes ===");
        for (Client c : list) {
            System.out.printf("ID:%d  Nombre:%s  Correo:%s  Tel:%s%n",
                    c.getClientId(), c.getName(), c.getEmail(), c.getPhone());
        }
    }

    /** Muestra un solo cliente por ID */
    public void showById(int clientId) {
        for (Client c : getAll()) {
            if (c.getClientId() == clientId) {
                System.out.println("=== Cliente Encontrado ===");
                System.out.printf("ID: %d%nNombre: %s%nCorreo: %s%nTeléfono: %s%n",
                        c.getClientId(), c.getName(), c.getEmail(), c.getPhone());
                return;
            }
        }
        System.out.println("No se encontró cliente con ID " + clientId);
    }

    private int getNextId() {
        List<Client> list = getAll();
        int maxId = 0;
        for (Client c : list) {
            if (c.getClientId() > maxId) {
                maxId = c.getClientId();
            }
        }
        return maxId + 1;
    }

    /** Menú interactivo de CRUD de clientes */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Gestión de Clientes —");
            System.out.println("1. Ver todos los clientes");
            System.out.println("2. Buscar cliente por ID");
            System.out.println("3. Agregar cliente");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver al menú principal");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (opt) {
                case 1 -> {

                    showAll();
                }
                case 2 -> {

                    int searchId = InputValidator.readValidatedInteger(sc, "ID de cliente a buscar: ");
                    showById(searchId);
                }
                case 3 -> {

                    int newId = getNextId();
                    System.out.println("Asignando ID de cliente: " + newId);
                    String newName = InputValidator.readValidatedText(sc, "Nombre: ");
                    String newEmail = InputValidator.readValidatedEmail(sc, "Correo: ");
                    String newTel = InputValidator.readValidatedPhone(sc, "Teléfono: ");
                    add(new Client(newId, newName, newEmail, newTel));
                }
                case 4 -> {

                    int updId = InputValidator.readValidatedInteger(sc, "ID del cliente a actualizar: ");
                    Client existing = null;
                    for (Client c : getAll()) {
                        if (c.getClientId() == updId) {
                            existing = c;
                            break;
                        }
                    }
                    if (existing == null) {
                        System.out.println("No se encontró cliente con ID " + updId);
                        break;
                    }
                    // Mostrar info actual
                    System.out.println("=== Información Actual ===");
                    System.out.printf("1. Nombre: %s%n2. Correo: %s%n3. Teléfono: %s%n4. Todos los campos%n",
                            existing.getName(), existing.getEmail(), existing.getPhone());
                    int fieldOpt = InputValidator.readValidatedInteger(sc, "¿Qué deseas actualizar? ");

                    String updatedName = existing.getName();
                    String updatedEmail = existing.getEmail();
                    String updatedTel = existing.getPhone();

                    switch (fieldOpt) {
                        case 1 -> updatedName = InputValidator.readValidatedText(sc, "Nuevo Nombre: ");
                        case 2 -> updatedEmail = InputValidator.readValidatedEmail(sc, "Nuevo Correo: ");
                        case 3 -> updatedTel = InputValidator.readValidatedPhone(sc, "Nuevo Teléfono: ");
                        case 4 -> {
                            updatedName = InputValidator.readValidatedText(sc, "Nuevo Nombre: ");
                            updatedEmail = InputValidator.readValidatedEmail(sc, "Nuevo Correo: ");
                            updatedTel = InputValidator.readValidatedPhone(sc, "Nuevo Teléfono: ");
                        }
                        default -> System.out.println("Opción inválida. No se realizaron cambios.");
                    }
                    updateClient(new Client(updId, updatedName, updatedEmail, updatedTel));
                }
                case 5 -> {

                    int delId = InputValidator.readValidatedInteger(sc, "ID del cliente a eliminar: ");
                    delete(delId);
                }
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
