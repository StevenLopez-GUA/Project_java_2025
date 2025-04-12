package persistence;

import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONManager {

    private static final Gson gson = new Gson();

    // Método genérico para escribir una lista de objetos a un archivo JSON
    public static <T> void writeList(String filePath, List<T> list) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            System.err.println("Error al escribir en " + filePath + ": " + e.getMessage());
        }
    }

    // Método genérico para leer una lista de objetos desde un archivo JSON
    public static <T> List<T> readList(String filePath, Type typeOfT) {
        List<T> result = new ArrayList<>();
        try (Reader reader = new FileReader(filePath)) {
            result = gson.fromJson(reader, typeOfT);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado (" + filePath + "), se creará uno nuevo.");
        } catch (IOException e) {
            System.err.println("Error al leer " + filePath + ": " + e.getMessage());
        }
        return result;
    }
}
