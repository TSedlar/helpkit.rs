package rs.helpkit.api.raw;

import rs.helpkit.internal.HookLoader;

import java.lang.invoke.MethodHandle;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Fields {

    /**
     * Gets the field's value of the given hook with its parent
     *
     * @param key The key of the hook to get a value for
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
                t.printStackTrace();
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
}
