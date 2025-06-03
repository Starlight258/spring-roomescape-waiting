package roomescape.application.reservation.dto;

import java.time.LocalDate;

public class ReservationSearchRequest {

    private final Long themeId;
    private final Long memberId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public ReservationSearchRequest(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}

