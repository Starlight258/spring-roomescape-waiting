package roomescape.infrastrcture.persistence.reservation;

import org.springframework.stereotype.Component;
import roomescape.domain.reservation.ReservationChecker;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

@Component
public class DatabaseReservationChecker implements ReservationChecker {

    private final ReservationJpaRepository reservationJpaRepository;

    public DatabaseReservationChecker(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public boolean isAlreadyReserved(ReservationDate date, ReservationTime time, Theme theme) {
        return reservationJpaRepository.existsByDateAndReservationTimeAndTheme(
                date.getDate(),
                time.getStartTime(),
                theme.getId()
        );
    }
}
