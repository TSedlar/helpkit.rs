package rs.helpkit.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Manifest {

    String author();
    String name();
    String description();
    double version();
    boolean loop() default true;
}
