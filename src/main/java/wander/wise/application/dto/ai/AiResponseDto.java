package wander.wise.application.dto.ai;

public record AiResponseDto(
        String fullName,
        String tripTypes,
        String climate,
        String specialRequirements,
        String description,
        String whyThisPlace) {
    public AiResponseDto setTripTypes(String newTripTypes) {
        return new AiResponseDto(
                this.fullName,
                newTripTypes,
                this.climate,
                this.specialRequirements,
                this.description,
                this.whyThisPlace);
    }

    public AiResponseDto setSpecialRequirements(String newSpecialRequirements) {
        return new AiResponseDto(
                this.fullName,
                this.tripTypes,
                this.climate,
                newSpecialRequirements,
                this.description,
                this.whyThisPlace);
    }
}
