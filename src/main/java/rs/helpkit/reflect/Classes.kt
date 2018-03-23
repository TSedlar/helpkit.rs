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
        return clazz.declaredFields.find(filter)?.apply {
            isAccessible = true
        }
    }

    /**
     * Finds the method matching the filter in the given class
     *
     * @param clazz The class to search within
     * @param filter The filter to match
     * @return The method matching the filter in the given class
     */
    fun findMethod(clazz: Class<*>, filter: (Method) -> Boolean): Method? {
        return clazz.declaredMethods.find(filter)?.apply {
            isAccessible = true
        }
    }
}
