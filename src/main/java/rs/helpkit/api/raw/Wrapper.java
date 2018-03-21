package rs.helpkit.api.raw;

import java.lang.ref.WeakReference;

/**
 * @author Tyler Sedlar
 * @since 3/20/2018
 */
public class Wrapper extends WeakReference<Object> {

    private final String container;

    public Wrapper(String container, Object referent) {
        super(referent);
        this.container = container;
    }

    private String key(String name) {
        return container + "#" + name;
    }

    protected Object get(String name) {
        return Fields.get(key(name), get());
    }

    protected int asInt(String name) {
        return Fields.asInt(key(name), get());
    }

    protected String asString(String name) {
        return Fields.asString(key(name), get());
    }
}
