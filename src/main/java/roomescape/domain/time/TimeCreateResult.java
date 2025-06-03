package roomescape.domain.time;

public class TimeCreateResult {

    private final boolean success;
    private final String message;
    private final ReservationTime reservationTime;

    private TimeCreateResult(boolean success, String message, ReservationTime reservationTime) {
        this.success = success;
        this.message = message;
        this.reservationTime = reservationTime;
    }

    public static TimeCreateResult success(ReservationTime reservationTime) {
        return new TimeCreateResult(true, "예약 시간이 생성되었습니다.", reservationTime);
    }

    public static TimeCreateResult failure(String message) {
        return new TimeCreateResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
