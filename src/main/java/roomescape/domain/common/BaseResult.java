package roomescape.domain.common;

public abstract class BaseResult implements Result {

    protected final boolean success;
    protected final String message;

    protected BaseResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public boolean isFailure() {
        return !success;
    }
}
