package roomescape.dto.response.reservation;

import roomescape.domain.reservation.Reservation;
import roomescape.dto.response.member.MemberRetrievalResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;

public record ReservationRetrievalResponse(Long id, String date, ReservationTimeRetrievalResponse time,
                                           ThemeRetrievalResponse theme,
                                           MemberRetrievalResponse member) {

    public static ReservationRetrievalResponse from(Reservation reservation) {
        ReservationTimeRetrievalResponse time = ReservationTimeRetrievalResponse.from(reservation.getTime());
        ThemeRetrievalResponse theme = ThemeRetrievalResponse.from(reservation.getTheme());
        MemberRetrievalResponse member = MemberRetrievalResponse.from(reservation.getMember());
        return new ReservationRetrievalResponse(reservation.getId(),
                reservation.getDate().getDate().toString(), time, theme, member);
    }
}
