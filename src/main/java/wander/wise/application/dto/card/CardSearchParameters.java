package wander.wise.application.dto.card;

import jakarta.validation.constraints.NotBlank;

public record CardSearchParameters(
        @NotBlank String startLocation,
        String[] tripTypes,
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
