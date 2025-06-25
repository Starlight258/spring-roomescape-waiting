package roomescape.dto.response.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.slot.Slot;
import roomescape.dto.response.member.MemberRetrievalResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;

public record ReservationPreservationResponse(Long id, LocalDate date,
                                              ReservationTimeRetrievalResponse time,
                                              MemberRetrievalResponse member,
                                              ThemeRetrievalResponse theme) {

    public static ReservationPreservationResponse from(final Reservation reservation) {
        Slot slot = reservation.getSlot();
        return new ReservationPreservationResponse(reservation.getId(), slot.getDate().getDate(),
                ReservationTimeRetrievalResponse.from(slot.getTime()),
                MemberRetrievalResponse.from(reservation.getMember()),
                ThemeRetrievalResponse.from(slot.getTheme())
        );
    }
}
