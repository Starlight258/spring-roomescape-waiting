package roomescape.dto.request.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegularReservationPreservationRequest(
        @NotBlank
        String date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId

) {

}
