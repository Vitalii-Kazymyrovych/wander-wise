package wander.wise.application.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import wander.wise.application.validation.map.link.MapLink;

public record CreateCardRequestDto(
        @NotBlank String name,
        @NotBlank String populatedLocality,
        @NotBlank String region,
        @NotBlank String country,
        @NotBlank String continent,
        @NotEmpty String[] tripTypes,
        @NotBlank String climate,
        @NotEmpty String[] specialRequirements,
        @NotBlank String description,
        @NotEmpty String[] whyThisPlace,
        String[] imageLinks,
        @NotBlank @MapLink String mapLink) {
}
