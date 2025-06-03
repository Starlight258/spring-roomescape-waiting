package roomescape.presentation.web.reservation.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.presentation.web.member.dto.response.MemberInfo;
import roomescape.presentation.web.theme.dto.response.ThemeInfo;
import roomescape.presentation.web.time.dto.response.TimeInfo;

public class AdminReservationResponse {

    private final Long id;
    private final MemberInfo member;
    private final LocalDate date;
    private final TimeInfo time;
    private final ThemeInfo theme;

    public AdminReservationResponse(final Long id, final MemberInfo member, final LocalDate date, final TimeInfo time,
                                    final ThemeInfo theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static AdminReservationResponse from(Reservation reservation) {
        return new AdminReservationResponse(
                reservation.getId(),
                MemberInfo.from(reservation.getMember()),
                reservation.getDate().getDate(),
                TimeInfo.from(reservation.getTime()),
                ThemeInfo.from(reservation.getTheme())
        );
    }

    public Long getId() {
        return id;
    }

    public MemberInfo getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeInfo getTime() {
        return time;
    }

    public ThemeInfo getTheme() {
        return theme;
    }
}

