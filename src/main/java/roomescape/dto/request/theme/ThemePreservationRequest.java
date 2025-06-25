package roomescape.dto.request.theme;

import jakarta.validation.constraints.NotBlank;

public record ThemePreservationRequest(
        @NotBlank
        String name,
        String description,
        String thumbnail
) {

}
