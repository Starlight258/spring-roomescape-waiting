package roomescape.dto.request.waiting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.slot.Slot;
import roomescape.domain.waiting.Waiting;

public record WaitingPreservationRequest(
        @NotBlank
        String date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId
) {
    public static WaitingPreservationRequest from(final Waiting waiting) {
        Slot slot = waiting.getSlot();
        return new WaitingPreservationRequest(
                slot.getDate().getDate().toString(),
                slot.getTime().getId(),
                slot.getTheme().getId());
    }

}
