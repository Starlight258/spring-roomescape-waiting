package roomescape.application.reservation;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationChecker;
import roomescape.domain.reservation.ReservationResult;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

@Component
public class ReservationFactory {

    private final ReservationChecker reservationChecker;

    public ReservationFactory(ReservationChecker reservationChecker) {
        this.reservationChecker = reservationChecker;
    }

    public ReservationResult create(Member member, LocalDate date,
                                    ReservationTime time, Theme theme) {
        // 도메인 팩토리 메서드 호출
        return Reservation.create(
                member,
                date,
                time,
                theme,
                reservationChecker
        );
    }
}
