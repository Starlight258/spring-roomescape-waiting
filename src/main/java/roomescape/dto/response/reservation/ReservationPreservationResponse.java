package roomescape.dto.response.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.response.member.MemberRetrievalResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;

public record ReservationPreservationResponse(Long id, LocalDate date,
                                              ReservationTimeRetrievalResponse time,
                                              MemberRetrievalResponse member,
                                              ThemeRetrievalResponse theme) {

    public static ReservationPreservationResponse from(final Reservation reservation) {
        return new ReservationPreservationResponse(reservation.getId(), reservation.getDate().getDate(),
                ReservationTimeRetrievalResponse.from(reservation.getTime()),
                MemberRetrievalResponse.from(reservation.getMember()),
                ThemeRetrievalResponse.from(reservation.getTheme())
        );
    }
}
