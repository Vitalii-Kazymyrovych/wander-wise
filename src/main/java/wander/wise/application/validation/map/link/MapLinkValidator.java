package wander.wise.application.validation.map.link;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MapLinkValidator implements ConstraintValidator<MapLink, String> {
    @Override
    public boolean isValid(String mapLink, ConstraintValidatorContext constraintValidatorContext) {
        return mapLink.startsWith("https://maps.app.goo.gl/")
                || mapLink.startsWith("https://www.google.com/maps/");
    }
}
