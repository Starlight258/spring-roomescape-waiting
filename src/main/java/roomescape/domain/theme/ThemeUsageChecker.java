package roomescape.domain.theme;

import java.time.LocalDate;

public interface ThemeUsageChecker {

    boolean isInUse(Long themeId);
    int getReservationCount(Long themeId);
    int getReservationCountInPeriod(Long themeId, LocalDate startDate, LocalDate endDate);
}
