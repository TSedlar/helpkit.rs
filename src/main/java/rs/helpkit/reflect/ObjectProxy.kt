package rs.helpkit.reflect

import javassist.util.proxy.Proxy
import javassist.util.proxy.ProxyFactory
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * @author Tyler Sedlar
 * @since 3/25/2018
 */
object ObjectProxy {

    fun callback(method: Method, args: Array<*>) {
        val tgs = method.toGenericString()
//        println("proxying: $tgs")
    }

    fun override(field: Field, initParameterTypes: Array<Class<*>>, initArgs: Array<Any>, parent: Any?): Boolean {
        val target = field.get(parent)

        val instance = try {
            val factory = ProxyFactory()
            factory.superclass = target.javaClass

            val proxy = factory.createClass()
            val construct = proxy.getConstructor(*initParameterTypes)
            construct.newInstance(*initArgs) as Proxy
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (instance != null) {
            target.javaClass.declaredFields.forEach {
                if (!Modifier.isStatic(it.modifiers)) {
                    Classes.setFieldAccessible(it)
                    it.set(instance, it.get(target))
                }
            }
            instance.setHandler { self, method, forwarder, args ->
                callback(method, args)
                return@setHandler forwarder.invoke(self, *args)
            }
            field.set(parent, instance)
            return true
        } else {
            return false
        }
    }

    fun override(field: Field, parent: Any?): Boolean = override(field, arrayOf(), arrayOf(), parent)
}
