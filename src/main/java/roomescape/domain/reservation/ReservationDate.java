package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationDate {

    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        validateNotPast(date);
        this.date = date;
    }

    private void validateNotPast(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜로는 예약할 수 없습니다.");
        }
    }

    public boolean isSameDate(ReservationDate other) {
        return this.date.equals(other.date);
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof final ReservationDate that)) {
            return false;
        }
        return Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDate());
    }
}
