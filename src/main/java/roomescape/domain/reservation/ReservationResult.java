package roomescape.domain.reservation;

import roomescape.domain.common.Result;

public class ReservationResult implements Result {

    private final boolean success;
    private final String message;
    private final Reservation reservation;

    private ReservationResult(boolean success, String message, Reservation reservation) {
        this.success = success;
        this.message = message;
        this.reservation = reservation;
    }

    public static ReservationResult success(Reservation reservation) {
        return new ReservationResult(true, "예약이 완료되었습니다.", reservation);
    }

    public static ReservationResult failure(String message) {
        return new ReservationResult(false, message, null);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Reservation getReservation() {
        if (!success) {
            throw new IllegalStateException("실패한 결과에서는 예약 객체를 가져올 수 없습니다.");
        }
        return reservation;
    }
}
