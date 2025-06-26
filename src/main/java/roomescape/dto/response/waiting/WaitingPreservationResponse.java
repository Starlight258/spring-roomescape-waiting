package roomescape.dto.response.waiting;

import java.time.LocalDate;
import roomescape.domain.slot.Slot;
import roomescape.domain.waiting.Waiting;
import roomescape.dto.response.member.MemberRetrievalResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;

public record WaitingPreservationResponse(Long id, LocalDate date,
                                          ReservationTimeRetrievalResponse time,
                                          MemberRetrievalResponse member,
                                          ThemeRetrievalResponse theme) {

    public static WaitingPreservationResponse from(final Waiting waiting) {
        Slot slot = waiting.getSlot();
        return new WaitingPreservationResponse(
                waiting.getId(),
                slot.getDate().getDate(),
                ReservationTimeRetrievalResponse.from(slot.getTime()),
                MemberRetrievalResponse.from(waiting.getMember()),
                ThemeRetrievalResponse.from(slot.getTheme())
        );
    }
}
