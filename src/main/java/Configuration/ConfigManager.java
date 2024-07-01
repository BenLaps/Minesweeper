package Configuration;

import com.google.gson.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class ConfigManager {

    private static final String CONFIG_FILE = "config.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Map.class, new ConfigDeserializer())
            .create();
    private static Map<String, Object> config = new HashMap<>();

    static {
        readConfig();
    }



    public static int getPart(String key) {
        Object goldValue = config.getOrDefault(key, 0);
        if (goldValue instanceof Double) {
            return ((Double) goldValue).intValue();
        } else if (goldValue instanceof String) {
            try {
                return Integer.parseInt((String) goldValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return (int) goldValue;
    }

    public static void setPart(String key, String part) {
        try {
            int value = Integer.parseInt(part);
            config.put(key, value);
        } catch (NumberFormatException e) {
            config.put(key, part);
        }
        saveConfig();
    }
    public static void addByPart(eConfigPart configPart,int count ) {
        int current = getPart ( configPart.getKey () );
        current+=count;
        setPart ( configPart.getKey (),String.valueOf ( current ) );
    }

    public static void saveConfig() {
        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        try (Reader reader = new FileReader(CONFIG_FILE)) {
            config = gson.fromJson(reader, config.getClass());
            if (config == null) {
                config = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            config = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}