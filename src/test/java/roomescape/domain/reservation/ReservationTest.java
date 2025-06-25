package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
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
        ReservationDate date = new ReservationDate(LocalDate.now());
        ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
        Theme theme = UnitTestFixture.makeTheme();

        // When & Then
        Assertions.assertThatCode(() -> Reservation.createReservation(date, time, theme, member))
                .doesNotThrowAnyException();
    }

    @Test
    void 과거_시간으로_예약은_불가능하다() {
        // Given
        Member member = UnitTestFixture.makeRegularMember();
        ReservationDate date = new ReservationDate(LocalDate.now());
        ReservationTime time = new ReservationTime(LocalTime.now().minusHours(1));
        Theme theme = UnitTestFixture.makeTheme();

        // When & Then
        Assertions.assertThatThrownBy(() -> Reservation.createReservation(date, time, theme, member))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Reservation date and time should be future");

    }
}
