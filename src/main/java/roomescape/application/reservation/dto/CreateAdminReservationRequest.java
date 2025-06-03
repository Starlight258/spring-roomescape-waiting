package roomescape.application.reservation.dto;

import java.time.LocalDate;

public class CreateAdminReservationRequest {

    private final LocalDate date;
    private final Long timeId;
    private final Long themeId;
    private final Long memberId;

    public CreateAdminReservationRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
        this.memberId = memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getMemberId() {
        return memberId;
    }
}

