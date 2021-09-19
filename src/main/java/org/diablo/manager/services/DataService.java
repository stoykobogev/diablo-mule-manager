package org.diablo.manager.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.diablo.manager.entities.Mule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class DataService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final File MULES_FILE;
    private static final File ITEM_NAMES_FILE;
    private static final AtomicReference<List<Mule>> MULES = new AtomicReference<>(new ArrayList<>());
    private static final AtomicReference<List<String>> ITEM_NAMES = new AtomicReference<>(new ArrayList<>());
    private static final List<Consumer<List<Mule>>> MULE_SUBSCRIBERS = new ArrayList<>();
    private static final List<Consumer<List<String>>> ITEM_SUBSCRIBERS = new ArrayList<>();

    static {
        MULES_FILE = new File("./mules.json");
        ITEM_NAMES_FILE = new File("./item-names.json");

        try {
            if (!MULES_FILE.createNewFile()) {
                MULES.set(readFile(MULES_FILE, Mule.class));
            }

            if (!ITEM_NAMES_FILE.createNewFile()) {
                ITEM_NAMES.set(readFile(ITEM_NAMES_FILE, String.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static List<Mule> getMules() {
        return MULES.get();
    }

    public static List<String> getItemNames() {
        return ITEM_NAMES.get();
    }

    public static void subscribeToMules(Consumer<List<Mule>> consumer) {
        MULE_SUBSCRIBERS.add(consumer);
    }

    public static void subscribeToItems(Consumer<List<String>> consumer) {
        ITEM_SUBSCRIBERS.add(consumer);
    }

    public static void saveMules(List<Mule> mules) {
        try {
            OBJECT_MAPPER.writeValue(MULES_FILE, mules);
            MULES.set(mules);

            MULE_SUBSCRIBERS.forEach(c -> c.accept(MULES.get()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void saveItemNames(List<String> itemNames) {
        try {
            OBJECT_MAPPER.writeValue(ITEM_NAMES_FILE, itemNames);
            ITEM_NAMES.set(itemNames);

            ITEM_SUBSCRIBERS.forEach(c -> c.accept(ITEM_NAMES.get()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static <T> List<T> readFile(File file, Class<T> clazz) {
        if (file.length() > 0) {
            try {
                return OBJECT_MAPPER.readValue(file,
                        TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return new ArrayList<>();
    }
}
