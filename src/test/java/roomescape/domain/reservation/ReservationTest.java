package roomescape.domain.reservation;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.BadRequestException;
import roomescape.fixture.UnitTestFixture;

class ReservationTest {

    @Test
    void 예약은_현재_시간_이후로만_가능하다() {
        // Given
        Member member = UnitTestFixture.makeRegularMember();
        LocalDateTime afterOneHour = LocalDateTime.now().plusHours(1);
        ReservationDate date = new ReservationDate(afterOneHour.toLocalDate());
        ReservationTime time = new ReservationTime(afterOneHour.toLocalTime());
        Theme theme = UnitTestFixture.makeTheme();

        // When & Then
        Assertions.assertThatCode(() -> Reservation.createReservation(date, time, theme, member))
                .doesNotThrowAnyException();
    }

    @Test
    void 과거_시간으로_예약은_불가능하다() {
        // Given
        Member member = UnitTestFixture.makeRegularMember();
        LocalDateTime beforeOneHour = LocalDateTime.now().minusHours(1);
        ReservationDate date = new ReservationDate(beforeOneHour.toLocalDate());
        ReservationTime time = new ReservationTime(beforeOneHour.toLocalTime());
        Theme theme = UnitTestFixture.makeTheme();

        // When & Then
        Assertions.assertThatThrownBy(() -> Reservation.createReservation(date, time, theme, member))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Reservation date and time should be future");

    }
}
