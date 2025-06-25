package roomescape.dto.request.reservationtime;

import jakarta.validation.constraints.NotBlank;

public record ReservationTimePreservationRequest(
        @NotBlank
        String startAt
) {

}
