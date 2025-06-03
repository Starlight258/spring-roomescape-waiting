package roomescape.domain.reservation;

import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public interface ReservationChecker {

    boolean isAlreadyReserved(ReservationDate date, ReservationTime time, Theme theme);

}
