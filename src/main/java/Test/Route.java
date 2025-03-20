package Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Make it available at runtime
@Target(ElementType.TYPE) // Can be applied to classes
public @interface Route {
    String value() default "/";
};
