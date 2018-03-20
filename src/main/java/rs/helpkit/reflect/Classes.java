package rs.helpkit.reflect;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Classes {

    /**
     * Finds the field matching the filter in the given class
     *
     * @param clazz The class to search within
     * @param filter The filter to match
     * @return The field matching the filter in the given class
     */
    public static Field findField(Class<?> clazz, Predicate<Field> filter) {
        for (Field field : clazz.getDeclaredFields()) {
            if (filter.test(field)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }
}
