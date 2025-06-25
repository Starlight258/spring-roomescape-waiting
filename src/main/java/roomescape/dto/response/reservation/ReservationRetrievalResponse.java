package roomescape.dto.response.reservation;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.slot.Slot;
import roomescape.dto.response.member.MemberRetrievalResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;

public record ReservationRetrievalResponse(Long id, String date, ReservationTimeRetrievalResponse time,
                                           ThemeRetrievalResponse theme,
                                           MemberRetrievalResponse member) {

    public static ReservationRetrievalResponse from(Reservation reservation) {
        Slot slot = reservation.getSlot();
        ReservationTimeRetrievalResponse time = ReservationTimeRetrievalResponse.from(slot.getTime());
        ThemeRetrievalResponse theme = ThemeRetrievalResponse.from(slot.getTheme());
        MemberRetrievalResponse member = MemberRetrievalResponse.from(reservation.getMember());
        return new ReservationRetrievalResponse(reservation.getId(),
                slot.getDate().getDate().toString(), time, theme, member);
    }
}
