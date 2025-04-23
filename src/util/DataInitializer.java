package util;

import com.google.gson.reflect.TypeToken;
import model.Phase;
import persistence.JSONManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataInitializer {
    private static final String PHASES_FILE     = "phases.json";
    private static final String CLIENTS_FILE    = "clients.json";
    private static final String COMPUTERS_FILE  = "computers.json";
    private static final String TECHS_FILE      = "technicians.json";
    private static final String HISTORY_FILE    = "historial.json";

    /**
     * Inicializa los archivos JSON necesarios si no existen o están vacíos.
     */
    public static void init() {
        initEmptyJson(CLIENTS_FILE);
        initEmptyJson(COMPUTERS_FILE);
        initEmptyJson(TECHS_FILE);
        initEmptyJson(HISTORY_FILE);
        initPhases();
    }

    /**
     * Crea un archivo JSON con un arreglo vacío si no existe o está vacío.
     */
    private static void initEmptyJson(String filePath) {
        Type listType = new TypeToken<List<Object>>() {}.getType();
        List<Object> list = JSONManager.readList(filePath, listType);
        if (list == null || list.isEmpty()) {
            JSONManager.writeList(filePath, new ArrayList<>());
        }
    }

    /**
     * Inicializa el archivo de fases con las 5 fases estándar si está vacío.
     */
    private static void initPhases() {
        Type phaseListType = new TypeToken<List<Phase>>() {}.getType();
        List<Phase> phases = JSONManager.readList(PHASES_FILE, phaseListType);
        if (phases == null || phases.isEmpty()) {
            phases = new ArrayList<>();
            phases.add(new Phase(1, "Recepción"));
            phases.add(new Phase(2, "Inspección"));
            phases.add(new Phase(3, "Reparación"));
            phases.add(new Phase(4, "Control de Calidad"));
            phases.add(new Phase(5, "Entrega"));
            JSONManager.writeList(PHASES_FILE, phases);
        }
    }
}
