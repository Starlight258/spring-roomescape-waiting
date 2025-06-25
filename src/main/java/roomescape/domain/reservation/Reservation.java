package roomescape.domain.reservation;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.domain.member.Member;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.slot.Slot;
import roomescape.domain.theme.Theme;
import roomescape.exception.BadRequestException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Slot slot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    public Reservation(final Slot slot, final Member member) {
        this.slot = slot;
        this.member = member;
    }

    public static Reservation createReservation(final ReservationDate date, final ReservationTime time,
                                                final Theme theme, final Member member) {
        validateFutureDateTime(date, time);
        Slot slot = new Slot(date, time, theme);
        return new Reservation(slot, member);
    }

    private static void validateFutureDateTime(final ReservationDate date, final ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date.getDate(), time.getStartAt());
        LocalDateTime nowDateTime = LocalDateTime.now();
        if (!dateTime.isAfter(nowDateTime)) {
            throw new BadRequestException("Reservation date and time should be future");
        }
    }
}
