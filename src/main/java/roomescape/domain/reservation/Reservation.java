package roomescape.domain.reservation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
    @Column(nullable = false)
    private ReservationDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "time_id")
    private ReservationTime time;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    public Reservation(final ReservationDate date, final ReservationTime time, final Theme theme, final Member member) {
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public static Reservation createReservation(final ReservationDate date, final ReservationTime time,
                                                final Theme theme, final Member member) {
        validateFutureDateTime(date, time);
        return new Reservation(date, time, theme, member);
    }

    private static void validateFutureDateTime(final ReservationDate date, final ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date.getDate(), time.getStartAt());
        LocalDateTime nowDateTime = LocalDateTime.now();
        if (!dateTime.isAfter(nowDateTime)) {
            throw new BadRequestException("Reservation date and time should be future");
        }
    }
}
