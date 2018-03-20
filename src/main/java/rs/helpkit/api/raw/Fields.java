package rs.helpkit.api.raw;

import rs.helpkit.internal.HookLoader;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Fields {

    /**
     * Sets the field's value of the given hook with its parent
     *
     * @param key    The key of the hook to set a value for
     * @param value  The value to set the field to
     * @param parent The parent, if it exists, otherwise <tt>null</tt>.
     */
    public static void set(String key, Object value, Object parent) {
        if (HookLoader.DIRECT_FIELDS.containsKey(key)) {
            Field field = HookLoader.DIRECT_FIELDS.get(key);
            try {
                field.set(parent, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the field's value of the given hook with its parent
     *
     * @param key    The key of the hook to set a value for
     * @param value  The value to set the field to
     */
    public static void set(String key, Object value) {
        set(key, value, null);
    }

    /**
     * Gets the field's value of the given hook with its parent
     *
     * @param key    The key of the hook to get a value for
     * @param parent The parent, if it exists, otherwise <tt>null</tt>.
     * @return The field's value
     */
    public static Object get(String key, Object parent) {
        MethodHandle handle = HookLoader.FIELDS.get(key);
        if (handle == null) {
            return null;
        } else {
            Object value;
            try {
                if (parent != null) {
                    value = handle.invoke(parent);
                } else {
                    value = handle.invoke();
                }
            } catch (Throwable t) {
                value = null;
            }
            if (HookLoader.MULTIPLIERS.containsKey(key) && value != null) {
                if (HookLoader.LONG_MAP.get(key)) {
                    value = ((long) value) * HookLoader.MULTIPLIERS.get(key).longValue();
                } else {
                    value = ((int) value) * HookLoader.MULTIPLIERS.get(key).intValue();
                }
            }
            return value;
        }
    }

    public static Object get(String key) {
        return get(key, null);
    }

    public static int asInt(String key, Object parent) {
        try {
            return (int) get(key, parent);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int asInt(String key) {
        return asInt(key, null);
    }

    public static String asString(String key, Object parent) {
        return Optional.ofNullable(get(key, parent)).map(Object::toString).orElse(null);
    }

    public static String asString(String key) {
        return asString(key, null);
    }
}
