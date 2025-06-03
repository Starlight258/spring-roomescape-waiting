package roomescape.domain.time;

public class TimeDeleteResult {

    private final boolean success;
    private final String message;

    private TimeDeleteResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static TimeDeleteResult success() {
        return new TimeDeleteResult(true, "예약 시간이 삭제되었습니다.");
    }

    public static TimeDeleteResult failure(String message) {
        return new TimeDeleteResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
