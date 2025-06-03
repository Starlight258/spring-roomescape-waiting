package roomescape.infrastrcture.persistence.theme;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import roomescape.domain.theme.ThemeUsageChecker;
import roomescape.infrastrcture.persistence.reservation.ReservationJpaRepository;

@Component
public class DatabaseThemeUsageChecker implements ThemeUsageChecker {

    private final ReservationJpaRepository reservationJpaRepository;

    public DatabaseThemeUsageChecker(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public boolean isInUse(Long themeId) {
        return reservationJpaRepository.existsByThemeId(themeId);
    }

    @Override
    public int getReservationCount(Long themeId) {
        return reservationJpaRepository.countByThemeId(themeId);
    }

    @Override
    public int getReservationCountInPeriod(Long themeId, LocalDate startDate, LocalDate endDate) {
        return reservationJpaRepository.countByThemeIdAndDateBetween(themeId, startDate, endDate);
    }
}
