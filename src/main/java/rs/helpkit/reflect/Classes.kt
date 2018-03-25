package rs.helpkit.reflect

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
object Classes {

    /**
     * Forces the given field to be accessible
     *
     * @param field The field to set accessible
     */
    fun setFieldAccessible(field: Field) {
        field.isAccessible = true
        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    }

    /**
     * Finds the field matching the filter in the given class
     *
     * @param clazz The class to search within
     * @param filter The filter to match
     * @return The field matching the filter in the given class
     */
    fun findField(clazz: Class<*>, filter: (Field) -> Boolean): Field? {
        return clazz.declaredFields.find(filter)?.apply {
            setFieldAccessible(this)
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
