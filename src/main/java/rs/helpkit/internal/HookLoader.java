package rs.helpkit.internal;

import rs.helpkit.pref.HKConfig;
import rs.helpkit.reflect.Classes;
import rs.helpkit.util.io.TSVListing;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class HookLoader {

    public static final Map<String, String> INVERSE_FIELD_MAP = new ConcurrentHashMap<>();
    public static final Map<String, Boolean> LONG_MAP = new ConcurrentHashMap<>();

    public static final Map<String, MethodHandle> FIELDS = new ConcurrentHashMap<>();
    public static final Map<String, Number> MULTIPLIERS = new ConcurrentHashMap<>();

    public static void load(ClassLoader loader) {
        loadFields(loader);
        loadMultipliers();
    }

    private static void loadFields(ClassLoader loader) {
        TSVListing fields = TSVListing.load(HKConfig.path(HKConfig.DATA, "fields.tsv"));
        fields.lines.remove(0); // remove header
        fields.lines.parallelStream().forEach(line -> {
            String key = line[0];
            if (line[1] == null || line[1].equals("null")) {
                System.out.println("BROKEN HOOK @ " + key);
            } else {
                INVERSE_FIELD_MAP.put(line[1], line[0]);
                String[] splits = line[1].split("\\.");
                String className = splits[0];
                String fieldName = splits[1];
                try {
                    Class<?> clazz = loader.loadClass(className);
                    Field field = Classes.findField(clazz, f -> f.getName().equals(fieldName));
                    if (field == null) {
                        System.out.println("HOOK NOT FOUND @ " + key);
                    } else {
                        LONG_MAP.put(key, field.getType().equals(long.class));
                        FIELDS.put(key, MethodHandles.lookup().unreflectGetter(field));
                    }
                } catch (ClassNotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void loadMultipliers() {
        TSVListing multipliers = TSVListing.load(HKConfig.path(HKConfig.DATA, "multipliers.tsv"));
        multipliers.lines.remove(0); // remove header
        multipliers.lines.parallelStream().forEach(line -> {
            if (INVERSE_FIELD_MAP.containsKey(line[0])) {
                String key = INVERSE_FIELD_MAP.get(line[0]);
                if (LONG_MAP.get(key)) {
                    MULTIPLIERS.put(key, Long.parseLong(line[1]));
                } else {
                    MULTIPLIERS.put(key, Integer.parseInt(line[1]));
                }
            }
        });
    }
}
