package roomescape.domain.theme;

import java.time.LocalDate;
import java.util.Objects;

public class PopularityScore {

    private final int reservationCount;
    private final LocalDate calculatedDate;

    public PopularityScore(int reservationCount) {
        if (reservationCount < 0) {
            throw new IllegalArgumentException("예약 건수는 음수일 수 없습니다.");
        }
        this.reservationCount = reservationCount;
        this.calculatedDate = LocalDate.now();
    }

    public boolean isMorePopularThan(PopularityScore other) {
        return this.reservationCount > other.reservationCount;
    }

    public boolean hasNoReservations() {
        return reservationCount == 0;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public LocalDate getCalculatedDate() {
        return calculatedDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PopularityScore that = (PopularityScore) obj;
        return reservationCount == that.reservationCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationCount);
    }
}
