package roomescape.service.regular;

import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.theme.Theme;

public interface ReservationDateCounterByTheme {

    Long count(ReservationDate startDate, ReservationDate endDate, Theme theme);
}
