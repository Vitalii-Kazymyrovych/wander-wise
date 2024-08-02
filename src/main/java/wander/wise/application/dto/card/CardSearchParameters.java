package wander.wise.application.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CardSearchParameters(
        @NotBlank String startLocation,
        @NotEmpty List<String> tripTypes,
        String[] climate,
        String[] specialRequirements,
        String[] travelDistance,
        String[] author) {
    public CardSearchParameters setTravelDistance(String newTravelDistance) {
        return new CardSearchParameters(
                this.startLocation,
                this.tripTypes,
                this.climate,
                this.specialRequirements,
                new String[]{newTravelDistance},
                this.author);
    }
}
