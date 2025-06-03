package roomescape.infrastrcture.persistence.time;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.time.TimeUsageChecker;
import roomescape.infrastrcture.persistence.reservation.ReservationJpaRepository;

@Component
public class DatabaseTimeUsageChecker implements TimeUsageChecker {

    private final ReservationJpaRepository reservationJpaRepository;

    public DatabaseTimeUsageChecker(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public boolean isTimeInUse(Long timeId) {
        return reservationJpaRepository.existsByReservationTimeId(timeId);
    }

    @Override
    public boolean hasReservationAt(LocalTime time, ReservationDate date, Long themeId) {
        return reservationJpaRepository.existsByTimeAndDateAndThemeId(time, date.getDate(), themeId);
    }
}
