package logic;

import com.google.gson.reflect.TypeToken;
import model.Record;
import persistence.JSONManager;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WarrantyManager {

    private static final String HISTORIAL_FILE = "historial.json"; // O el nombre de tu archivo de historial

    /**
     * Mueve una computadora a una nueva fase.
     *
     * @param serviceTag  El identificador de la computadora.
     * @param nuevaPhase  El identificador de la nueva fase (por ejemplo, 1=Recepción, etc.).
     * @param technicalId El ID del técnico (puede ser null si no aplica).
     * @param details     Información adicional sobre el movimiento.
     */
    public void moverComputadora(String serviceTag, int nuevaPhase, Integer technicalId, String details) {
        // Formateador de fecha ISO-8601
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String currentDateTime = LocalDateTime.now().format(formatter);

        // Leer la lista actual de registros de historial
        Type listType = new TypeToken<List<Record>>(){}.getType();
        List<Record> historial = JSONManager.readList(HISTORIAL_FILE, listType);

        // Buscar el último registro de esta computadora que aún no tenga fecha de salida (endDateTime)
        for (Record r : historial) {
            if (r.getServiceTag().equals(serviceTag) && r.getDepartureDate() == null) {
                // Cerrar el registro actual asignando la fecha de salida
                r.setDepartureDate(currentDateTime);
                break;
            }
        }

        // Crear un nuevo registro para la nueva fase
        int newRecordId = historial.size() + 1;  // Simple contador
        Record nuevoRegistro = new Record(
            newRecordId,
            serviceTag,
            nuevaPhase,
            technicalId,
            currentDateTime,
            null,
            details
        );
        historial.add(nuevoRegistro);

        // Guardar cambios en el archivo JSON
        JSONManager.writeList(HISTORIAL_FILE, historial);
        System.out.println("La computadora " + serviceTag + " se ha movido a la fase " + nuevaPhase + ".");
    }

    // Agrega aquí otros métodos que necesites (mostrarHistorial, etc.)
}
