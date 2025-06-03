package roomescape.infrastrcture.persistence.reservation;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.reservation.ReservationChecker;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public class InMemoryReservationChecker implements ReservationChecker {

    private final List<ReservationSlot> reservedSlots = new ArrayList<>();

    @Override
    public boolean isAlreadyReserved(ReservationDate date, ReservationTime time, Theme theme) {
        return reservedSlots.stream()
                .anyMatch(slot -> slot.matches(date, time, theme));
    }

    public void addReservation(ReservationDate date, ReservationTime time, Theme theme) {
        reservedSlots.add(new ReservationSlot(date, time, theme));
    }

    public void clear() {
        reservedSlots.clear();
    }

    // 내부 클래스
    private static class ReservationSlot {
        private final ReservationDate date;
        private final ReservationTime time;
        private final Theme theme;

        public ReservationSlot(ReservationDate date, ReservationTime time, Theme theme) {
            this.date = date;
            this.time = time;
            this.theme = theme;
        }

        public boolean matches(ReservationDate date, ReservationTime time, Theme theme) {
            return this.date.equals(date) &&
                    this.time.isSameTime(time) &&
                    this.theme.isSameTheme(theme);
        }
    }
}

