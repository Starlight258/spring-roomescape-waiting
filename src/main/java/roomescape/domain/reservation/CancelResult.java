package roomescape.domain.reservation;

import roomescape.domain.common.Result;

public class CancelResult implements Result {

    private final boolean success;
    private final String message;

    private CancelResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static CancelResult success() {
        return new CancelResult(true, "예약이 취소되었습니다.");
    }

    public static CancelResult failure(String message) {
        return new CancelResult(false, message);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


