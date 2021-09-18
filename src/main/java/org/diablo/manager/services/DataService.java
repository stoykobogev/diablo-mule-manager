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
    private static final File DATA_FILE;
    private static final AtomicReference<List<Mule>> MULES = new AtomicReference<>(new ArrayList<>());
    private static final List<Consumer<List<Mule>>> SUBSCRIBERS = new ArrayList<>();

    static {
        DATA_FILE = new File("./data.json");

        try {
            if (!DATA_FILE.createNewFile()) {
                MULES.set(readMules());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static List<Mule> getMules() {
        return MULES.get();
    }

    public static void subscribe(Consumer<List<Mule>> consumer) {
        SUBSCRIBERS.add(consumer);
    }

    public static void saveMules(List<Mule> mules) {
        try {
            OBJECT_MAPPER.writeValue(DATA_FILE, mules);
            MULES.set(mules);

            SUBSCRIBERS.forEach(c -> c.accept(MULES.get()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<Mule> readMules() {
        if (DATA_FILE.length() > 0) {
            try {
                return OBJECT_MAPPER.readValue(DATA_FILE,
                        TypeFactory.defaultInstance().constructCollectionType(List.class, Mule.class));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return new ArrayList<>();
    }
}
