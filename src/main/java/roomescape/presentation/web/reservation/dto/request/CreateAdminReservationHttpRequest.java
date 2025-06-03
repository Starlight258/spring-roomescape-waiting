package roomescape.presentation.web.reservation.dto.request;

import java.time.LocalDate;

public class CreateAdminReservationHttpRequest {
    private LocalDate date;
    private Long timeId;
    private Long themeId;
    private Long memberId;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public void setTimeId(final Long timeId) {
        this.timeId = timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(final Long themeId) {
        this.themeId = themeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(final Long memberId) {
        this.memberId = memberId;
    }
}

