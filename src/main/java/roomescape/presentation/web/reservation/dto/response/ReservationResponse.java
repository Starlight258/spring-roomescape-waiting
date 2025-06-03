package roomescape.presentation.web.reservation.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.presentation.web.theme.dto.response.ThemeResponse;
import roomescape.presentation.web.time.dto.response.TimeResponse;

public class ReservationResponse {

    private final Long id;
    private final LocalDate date;
    private final TimeResponse time;
    private final ThemeResponse theme;

    public ReservationResponse(Long id, LocalDate date,
                               TimeResponse time, ThemeResponse theme) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    // ✅ 도메인 객체에서 응답 DTO로 변환
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate().getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public Long getId() {
        return id;
    }


    public LocalDate getDate() {
        return date;
    }

    public TimeResponse getTime() {
        return time;
    }

    public ThemeResponse getTheme() {
        return theme;
    }
}

