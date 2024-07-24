package wander.wise.application.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import wander.wise.application.validation.map.link.MapLink;

public record CreateCardRequestDto(
        @NotBlank String name,
        @NotBlank String populatedLocality,
        String region,
        @NotBlank String country,
        String continent,
        @NotEmpty String[] tripTypes,
        @NotBlank String climate,
        @NotEmpty String[] specialRequirements,
        @NotBlank String description,
        @NotEmpty String[] whyThisPlace,
        String[] imageLinks,
        @NotBlank @MapLink String mapLink) {
    public CreateCardRequestDto setRegionAndContinent(String region, String continent) {
        return new CreateCardRequestDto(
                this.name,
                this.populatedLocality,
                region,
                this.country,
                continent,
                this.tripTypes,
                this.climate,
                this.specialRequirements,
                this.description,
                this.whyThisPlace,
                this.imageLinks,
                this.mapLink);
    }
}
