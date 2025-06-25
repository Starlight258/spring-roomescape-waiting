package roomescape.dto.response.waiting;

import roomescape.domain.slot.Slot;
import roomescape.domain.waiting.Waiting;
import roomescape.dto.response.member.MemberRetrievalResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;

public record WaitingRetrievalResponse(Long id, String date, ReservationTimeRetrievalResponse time,
                                       ThemeRetrievalResponse theme,
                                       MemberRetrievalResponse member) {

    public static WaitingRetrievalResponse from(Waiting waiting) {
        Slot slot = waiting.getSlot();
        ReservationTimeRetrievalResponse time = ReservationTimeRetrievalResponse.from(slot.getTime());
        ThemeRetrievalResponse theme = ThemeRetrievalResponse.from(slot.getTheme());
        MemberRetrievalResponse member = MemberRetrievalResponse.from(waiting.getMember());
        return new WaitingRetrievalResponse(waiting.getId(),
                slot.getDate().getDate().toString(), time, theme, member);
    }
}
