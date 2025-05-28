// src/auth/AuthenticationService.java
package auth;

import com.google.gson.reflect.TypeToken;
import model.Technical;
import persistence.JSONManager;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class AuthenticationService {

    /* ---------- Config ---------- */
    private static final String TECHS_FILE = "technicians.json";
    private static final Type TECH_LIST_TYPE = new TypeToken<List<Technical>>() {
    }.getType();
    /* ---------------------------- */

    private Technical loggedIn; // mantiene la sesión en memoria

    /* ====== API pública ====== */

    /** Registra un nuevo técnico (contraseña se guarda con BCrypt). */
    public Technical register(String nombre,
            String email,
            String plainPassword) {

        List<Technical> list = getAll();

        // 1. Evitar correos duplicados
        Optional<Technical> dup = list.stream()
                .filter(t -> t.getEmailTechnical().equalsIgnoreCase(email))
                .findFirst();
        if (dup.isPresent()) {
            throw new IllegalArgumentException("Ese correo ya está registrado.");
        }

        // 2. Generar hash y nuevo ID
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        int nextId = list.stream()
                .mapToInt(Technical::getTechnicalId)
                .max().orElse(0) + 1;

        Technical t = new Technical(nextId, nombre, email, hash);
        list.add(t);
        saveAll(list);
        return t;
    }

    /** Devuelve true si el login es correcto y fija la sesión. */
    public boolean login(String email, String plainPassword) {
        List<Technical> list = getAll();
        Optional<Technical> opt = list.stream()
                .filter(t -> {
                    String e = t.getEmailTechnical();
                    return e != null && e.equalsIgnoreCase(email);
                })
                .findFirst();

        if (opt.isPresent()
                && BCrypt.checkpw(plainPassword, opt.get().getPassword())) {
            loggedIn = opt.get();
            return true;
        }
        return false;
    }

    public void logout() {
        loggedIn = null;
    }

    public boolean isLoggedIn() {
        return loggedIn != null;
    }

    public Technical currentUser() {
        return loggedIn;
    }

    /* ====== utilidades privadas ====== */

    private List<Technical> getAll() {
        return JSONManager.readList(TECHS_FILE, TECH_LIST_TYPE);
    }

    private void saveAll(List<Technical> list) {
        JSONManager.writeList(TECHS_FILE, list);
    }
}
