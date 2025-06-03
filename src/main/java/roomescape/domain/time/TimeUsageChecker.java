package roomescape.domain.time;

import java.time.LocalTime;
import roomescape.domain.reservation.ReservationDate;

public interface TimeUsageChecker {

    boolean isTimeInUse(Long timeId);

    boolean hasReservationAt(LocalTime time, ReservationDate date, Long themeId);
}
