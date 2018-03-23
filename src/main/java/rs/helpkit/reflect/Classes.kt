package rs.helpkit.reflect

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Classes {

    /**
     * Finds the field matching the filter in the given class
     *
     * @param clazz The class to search within
     * @param filter The filter to match
     * @return The field matching the filter in the given class
     */
    fun findField(clazz: Class<*>, filter: (Field) -> Boolean): Field? {
        for (field in clazz.declaredFields) {
            if (filter(field)) {
                field.isAccessible = true
                return field
            }
        }
        return null
    }

    /**
     * Finds the method matching the filter in the given class
     *
     * @param clazz The class to search within
     * @param filter The filter to match
     * @return The method matching the filter in the given class
     */
    fun findMethod(clazz: Class<*>, filter: (Method) -> Boolean): Method? {
        for (method in clazz.declaredMethods) {
            if (filter(method)) {
                method.isAccessible = true
                return method
            }
        }
        return null
    }
}
