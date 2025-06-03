package roomescape.domain.reservation;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public class Reservations {

    private final List<Reservation> reservations;

    public Reservations(List<Reservation> reservations) {
        this.reservations = new ArrayList<>(reservations);
    }

    // 특정 조건으로 필터링
    public Reservations filterBy(ReservationSearchCondition condition) {
        List<Reservation> filtered = reservations.stream()
                .filter(condition::matches)
                .toList();
        return new Reservations(filtered);
    }

    // 특정 멤버의 예약만 조회
    public Reservations filterByMember(Member member) {
        List<Reservation> memberReservations = reservations.stream()
                .filter(reservation -> reservation.isOwnedBy(member))
                .toList();
        return new Reservations(memberReservations);
    }

    // 중복 예약 체크
    public boolean hasConflict(ReservationDate date, ReservationTime time, Theme theme) {
        return reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED)
                .anyMatch(reservation -> reservation.isSameSlot(date, time, theme));
    }

    public List<Reservation> toList() {
        return new ArrayList<>(reservations);
    }

    public int size() {
        return reservations.size();
    }
}
