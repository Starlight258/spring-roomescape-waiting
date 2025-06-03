package roomescape.domain.reservation;

public enum ReservationStatus {

    CONFIRMED("예약 확정"),
    CANCELLED("예약 취소"),
    PENDING_PAYMENT("결제 대기");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public boolean canCancel() {
        return this == CONFIRMED || this == PENDING_PAYMENT;
    }
}
