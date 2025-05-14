package controllers;

import com.google.gson.reflect.TypeToken;
import model.Record;
import model.Technical;
import org.mindrot.jbcrypt.BCrypt;
import persistence.JSONManager;
import util.InputValidator;
import util.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class TechnicalController {

    private static final String TECHS_FILE = "technicians.json";

    /* ---------- utilidades internas ---------- */
    private List<Technical> getAll() {
        Type listType = new TypeToken<List<Technical>>() {
        }.getType();
        return JSONManager.readList(TECHS_FILE, listType);
    }

    private void saveAll(List<Technical> list) {
        JSONManager.writeList(TECHS_FILE, list);
    }

    private int getNextId() {
        return getAll().stream()
                .mapToInt(Technical::getTechnicalId)
                .max().orElse(0) + 1;
    }
    /* ----------------------------------------- */

    /* ============= CRUD ======================= */
    public void showAll() {
        System.out.println("=== Lista de Técnicos ===");
        getAll().forEach(t -> System.out.printf("ID:%d  Nombre:%s  Correo:%s%n",
                t.getTechnicalId(), t.getNameTechnical(), t.getEmailTechnical()));
    }

    public void ensureAdmin() {
        List<Technical> list = getAll();
        String adminEmail = "admini@gmail.com";
        boolean exists = list.stream()
                .map(Technical::getEmailTechnical)
                .anyMatch(e -> e != null && e.equalsIgnoreCase(adminEmail));
        if (!exists) {
            String hash = BCrypt.hashpw("Admin123!", BCrypt.gensalt());
            Technical admin = new Technical(getNextId(), "Admin", adminEmail, hash);
            list.add(admin);
            saveAll(list);
            System.out.println("Administrador creado: " + adminEmail + " / Admin123!");
        }
    }

    public void showById(int id) {
        getAll().stream()
                .filter(t -> t.getTechnicalId() == id)
                .findFirst()
                .ifPresentOrElse(t -> {
                    System.out.println("=== Técnico Encontrado ===");
                    System.out.printf("ID:%d%nNombre:%s%nCorreo:%s%n",
                            t.getTechnicalId(), t.getNameTechnical(), t.getEmailTechnical());
                }, () -> System.out.println("No se encontró técnico con ID " + id));
    }

    public void add(Scanner sc) {
        int id = getNextId();
        String nombre = InputValidator.readValidatedText(sc, "Nombre del técnico: ");
        String email = InputValidator.readValidatedEmail(sc, "Correo electrónico: ");
        String pass = InputValidator.readPassword(sc, "Contraseña: ");

        String hash = BCrypt.hashpw(pass, BCrypt.gensalt());
        Technical t = new Technical(id, nombre, email, hash);

        List<Technical> list = getAll();
        list.add(t);
        saveAll(list);

        System.out.printf("Técnico agregado → ID:%d  %s%n", id, nombre);
    }

    public void update(Scanner sc) {
        int id = InputValidator.readValidatedInteger(sc, "ID de técnico a actualizar: ");
        List<Technical> list = getAll();
        Technical existing = list.stream()
                .filter(t -> t.getTechnicalId() == id)
                .findFirst().orElse(null);

        if (existing == null) {
            System.out.println("No se encontró técnico con ID " + id);
            return;
        }

        /* --- pedir nuevos valores (Enter = mantener) --- */
        System.out.println("Nombre actual: " + existing.getNameTechnical());
        System.out.print("Nuevo nombre (Enter p/ mantener): ");
        String nuevoNombre = sc.nextLine().trim();

        System.out.println("Correo actual: " + existing.getEmailTechnical());
        System.out.print("Nuevo correo (Enter p/ mantener): ");
        String nuevoEmail = sc.nextLine().trim();

        String nuevaPass = InputValidator.readPassword(
                sc, "Nueva contraseña (Enter p/ mantener): ", true);

        /* --- si está vacío, usa el valor previo --- */
        if (nuevoNombre.isBlank())
            nuevoNombre = existing.getNameTechnical();
        if (nuevoEmail.isBlank())
            nuevoEmail = existing.getEmailTechnical();
        String nuevoHash = nuevaPass.isBlank()
                ? existing.getPassword()
                : BCrypt.hashpw(nuevaPass, BCrypt.gensalt());

        Technical updated = new Technical(id, nuevoNombre, nuevoEmail, nuevoHash);

        /* reemplazar en la lista y persistir */
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTechnicalId() == id) {
                list.set(i, updated);
                break;
            }
        }
        saveAll(list);
        System.out.println("Técnico actualizado.");
    }

    public void delete(Scanner sc) {
        int id = InputValidator.readValidatedInteger(sc, "ID de técnico a eliminar: ");

        /* ––– verificar uso en historial ––– */
        Type recType = new TypeToken<List<Record>>() {
        }.getType();
        List<Record> historial = JSONManager.readList("historial.json", recType);
        boolean usado = historial.stream()
                .anyMatch(r -> r.getTechnicalId() != null
                        && r.getTechnicalId() == id);
        if (usado) {
            System.out.println("No se puede eliminar: técnico referenciado en historial.");
            return;
        }

        /* ––– eliminar ––– */
        List<Technical> list = getAll();
        if (list.removeIf(t -> t.getTechnicalId() == id)) {
            saveAll(list);
            System.out.println("Técnico eliminado (ID " + id + ").");
        } else {
            System.out.println("No se encontró técnico con ID " + id);
        }
    }
    /* =========================================== */

    /* ============= Menú CLI ==================== */
    public void menu(Scanner sc) {
        int opt;
        do {
            Utils.clearConsole();
            System.out.println("— Gestión de Técnicos —");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Agregar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opt = InputValidator.readValidatedInteger(sc, "Opción: ");

            switch (opt) {
                case 1 -> showAll();
                case 2 -> showById(InputValidator.readValidatedInteger(sc, "ID: "));
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
                System.out.println("\nPresiona Enter para continuar…");
                sc.nextLine();
                Utils.clearConsole();
            }
        } while (opt != 0);
    }
}
