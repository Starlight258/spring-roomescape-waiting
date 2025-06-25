package roomescape.domain.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationDate {

    @Column(nullable = false)
    private LocalDate date;

    public ReservationDate(final LocalDate date) {
        this.date = date;
    }
}
