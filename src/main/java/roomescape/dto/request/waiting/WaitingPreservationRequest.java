package roomescape.dto.request.waiting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WaitingPreservationRequest(
        @NotBlank
        String date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId
) {
}
