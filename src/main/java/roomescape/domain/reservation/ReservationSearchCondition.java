package roomescape.domain.reservation;

import java.time.LocalDate;

public class ReservationSearchCondition {

    private final Long themeId;
    private final Long memberId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public ReservationSearchCondition(Long themeId, Long memberId,
                                      LocalDate dateFrom, LocalDate dateTo) {
        this.themeId = themeId;
        this.memberId = memberId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public boolean matches(Reservation reservation) {
        if (themeId != null && !reservation.getTheme().getId().equals(themeId)) {
            return false;
        }
        if (memberId != null && !reservation.getMember().getId().equals(memberId)) {
            return false;
        }
        if (dateFrom != null && reservation.getDate().getDate().isBefore(dateFrom)) {
            return false;
        }
        if (dateTo != null && reservation.getDate().getDate().isAfter(dateTo)) {
            return false;
        }
        return true;
    }
}
