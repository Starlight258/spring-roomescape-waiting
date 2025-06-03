package roomescape.domain.theme;

public class ThemeDeleteResult {

    private final boolean success;
    private final String message;

    private ThemeDeleteResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ThemeDeleteResult success() {
        return new ThemeDeleteResult(true, "테마가 삭제되었습니다.");
    }

    public static ThemeDeleteResult failure(String message) {
        return new ThemeDeleteResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
