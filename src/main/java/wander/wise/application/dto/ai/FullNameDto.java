package wander.wise.application.dto.ai;

public record FullNameDto(
        String name,
        String populatedLocality,
        String region,
        String country,
        String continent) {
}