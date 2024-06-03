package wander.wise.application.validation.map.link;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MapLinkValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MapLink {
    String message() default """
            Invalid map link. Valid map link should starts with 
            https://maps.app.goo.gl/ or https://www.google.com/maps/place/. 
            You can get it by selecting certain place on the map and copying 
            it's url or pressing "share" button.
            """;
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
