package roomescape.domain.theme;

public class ThemeCreateResult {

    private final boolean success;
    private final String message;
    private final Theme theme;

    private ThemeCreateResult(boolean success, String message, Theme theme) {
        this.success = success;
        this.message = message;
        this.theme = theme;
    }

    public static ThemeCreateResult success(Theme theme) {
        return new ThemeCreateResult(true, "테마가 생성되었습니다.", theme);
    }

    public static ThemeCreateResult failure(String message) {
        return new ThemeCreateResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Theme getTheme() {
        return theme;
    }
}
