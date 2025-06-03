package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public class Reservation {

    private Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    // 생성자 (팩토리 메서드를 통해서만 생성)
    private Reservation(Member member,
                        ReservationDate date, ReservationTime time, Theme theme) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = ReservationStatus.PENDING_PAYMENT;
        this.createdAt = LocalDateTime.now();
    }

    // 팩토리 메서드 - 예약 생성의 핵심 비즈니스 로직
    public static ReservationResult create(Member member,
                                           LocalDate date, ReservationTime time,
                                           Theme theme, ReservationChecker checker) {
        try {
            ReservationDate reservationDate = new ReservationDate(date);

            // 시간 유효성 검증
            if (time.isPastTime(reservationDate)) {
                return ReservationResult.failure("과거 시간으로는 예약할 수 없습니다.");
            }

            // 중복 예약 검증 (외부 서비스에 위임)
            if (checker.isAlreadyReserved(reservationDate, time, theme)) {
                return ReservationResult.failure("이미 예약된 시간입니다.");
            }

            Reservation reservation = new Reservation(member, reservationDate, time, theme);
            return ReservationResult.success(reservation);

        } catch (IllegalArgumentException e) {
            return ReservationResult.failure(e.getMessage());
        }
    }

    public static Reservation restore(final Long id, final Member member, final ReservationDate reservationDate,
                                      final ReservationTime reservationTime, final Theme theme,
                                      final ReservationStatus status, final LocalDateTime createdAt) {
        Reservation reservation = new Reservation(member, reservationDate, reservationTime, theme);
        reservation.id = id;
        reservation.status = status;
        reservation.createdAt = createdAt;
        return reservation;
    }

    // 예약 확정 (결제 완료 후)
    public void confirm() {
        if (this.status != ReservationStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("결제 대기 상태에서만 확정할 수 있습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    // 예약 취소
    public CancelResult cancel() {
        if (!this.status.canCancel()) {
            return CancelResult.failure("취소할 수 없는 예약 상태입니다.");
        }

        if (isPastReservation()) {
            return CancelResult.failure("과거 예약은 취소할 수 없습니다.");
        }

        this.status = ReservationStatus.CANCELLED;
        return CancelResult.success();
    }

    // 예약 시간이 지났는지 확인
    public boolean isPastReservation() {
        return this.time.isPastTime(this.date);
    }

    // 특정 조건과 일치하는지 확인 (검색용)
    public boolean matches(ReservationSearchCondition condition) {
        return condition.matches(this);
    }

    // 같은 시간/테마인지 확인 (중복 체크용)
    public boolean isSameSlot(ReservationDate date, ReservationTime time, Theme theme) {
        return this.date.isSameDate(date) &&
                this.time.isSameTime(time) &&
                this.theme.equals(theme);
    }

    // 멤버 소유 확인
    public boolean isOwnedBy(Member member) {
        return this.member.equals(member);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
