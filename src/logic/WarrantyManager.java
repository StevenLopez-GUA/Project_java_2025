package logic;

import com.google.gson.reflect.TypeToken;
import model.Record;
import persistence.JSONManager;
import util.InputValidator;
import auth.AuthenticationService;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WarrantyManager {

    private static final String HISTORIAL_FILE = "historial.json"; // O el nombre de tu archivo de historial


    private final AuthenticationService auth;

    /** Constructor que recibe el servicio de autenticación */
    public WarrantyManager(AuthenticationService auth) {
        this.auth = auth;
    }

    /** Constructor por defecto (sin autenticación) */
    public WarrantyManager() {
        this(null);
    }


    /**
     * Mueve una computadora a una nueva fase.
     *
     * @param serviceTag  El identificador de la computadora.
     * @param nuevaPhase  El identificador de la nueva fase (por ejemplo,
     *                    1=Recepción, etc.).
     * @param technicalId El ID del técnico (puede ser null si no aplica).
     * @param details     Información adicional sobre el movimiento.
     *
     *
     *
     *                    Encola el registro inicial (fase Recepción) de una
     *                    computadora recién
     *                    registrada.
     * 
     * @param serviceTag  Identificador de la computadora.
     * @param ignored     Segundo parámetro (puedes pasarlo pero no se usa para la
     *                    fase inicial).
     */
    public void enqueueInitialPhase(String serviceTag, Integer ignored) {
        // 1) Fecha/hora actual en ISO-8601
        String now = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 2) Leer el historial completo
        Type listType = new TypeToken<List<Record>>() {
        }.getType();
        List<Record> historial = JSONManager.readList(HISTORIAL_FILE, listType);

        // 3) Crear nuevo Record para fase 1 (Recepción)
        int newId = historial.size() + 1;
        Integer techId = (auth != null && auth.isLoggedIn())
                ? auth.currentUser().getTechnicalId()
                : null;

                Record rec = new Record(
                    newId,
                    serviceTag,
                    1,             // fase
                    techId,        // technicalId desde sesión
                    now,
                    null,
                    "Ingreso en Recepción");

        // 4) Añadir y guardar
        historial.add(rec);
        JSONManager.writeList(HISTORIAL_FILE, historial);
    }

    public void moverComputadora(String serviceTag, int nuevaPhase, Integer technicalId, String details) {
        // Formateador de fecha ISO-8601
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String currentDateTime = LocalDateTime.now().format(formatter);

        // Leer la lista actual de registros de historial
        Type listType = new TypeToken<List<Record>>() {
        }.getType();
        List<Record> historial = JSONManager.readList(HISTORIAL_FILE, listType);

        // Buscar el último registro de esta computadora que aún no tenga fecha de
        // salida (endDateTime)
        for (Record r : historial) {
            if (r.getServiceTag().equals(serviceTag) && r.getDepartureDate() == null) {
                // Cerrar el registro actual asignando la fecha de salida
                r.setDepartureDate(currentDateTime);
                break;
            }
        }

        // Crear un nuevo registro para la nueva fase
        int newRecordId = historial.size() + 1; // Simple contador
        Record nuevoRegistro = new Record(
                newRecordId,
                serviceTag,
                nuevaPhase,
                technicalId,
                currentDateTime,
                null,
                details);
        historial.add(nuevoRegistro);

        // Guardar cambios en el archivo JSON
        JSONManager.writeList(HISTORIAL_FILE, historial);
        System.out.println("La computadora " + serviceTag + " se ha movido a la fase " + nuevaPhase + ".");
    }

    /**
     * Devuelve la lista de serviceTags que están ENCOLADOS en una fase dada.
     * Busca los registros cuyo último Record para ese serviceTag tiene phaseId ==
     * pId y endDateTime == null.
     */
    public List<String> getQueue(int pId) {
        Type listType = new TypeToken<List<Record>>() {
        }.getType();
        List<Record> all = JSONManager.readList(HISTORIAL_FILE, listType);
        Map<String, Record> lastByTag = new LinkedHashMap<>();
        // Mantener orden de inserción
        for (Record r : all) {
            lastByTag.put(r.getServiceTag(), r);
        }
        List<String> queue = new ArrayList<>();
        for (Record r : lastByTag.values()) {
            if (r.getPhaseId() == pId && r.getDepartureDate() == null) {
                queue.add(r.getServiceTag());
            }
        }
        return queue;
    }

    /**
     * Procesa el siguiente equipo en la cola pId:
     * - Toma el primer serviceTag de la cola
     * - Pide técnico (o null) y detalles
     * - Llama a moverComputadora(...) para pasarlo a la fase siguiente
     */
    public void processNextInPhase(int pId, Scanner sc) {
        List<String> queue = getQueue(pId);
        if (queue.isEmpty()) {
            System.out.println("No hay equipos en la cola de fase " + pId);
            return;
        }
        String tag = queue.get(0);
        System.out.println("Procesando siguiente equipo: " + tag);
        int techRaw = InputValidator.readValidatedInteger(sc,
                "ID de Técnico para este paso (0 si no aplica): ");
        Integer tech = (techRaw == 0 ? null : techRaw);
        String details = InputValidator.readValidatedText(sc,
                "Detalles del trabajo en esta fase: ");
        // fase siguiente = pId + 1 (o lógica si hay saltos)
        moverComputadora(tag, pId + 1, tech, details);
        System.out.println("Equipo " + tag + " movido a la siguiente fase.");
    }

}
