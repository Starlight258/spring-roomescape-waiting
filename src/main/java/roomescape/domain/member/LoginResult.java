package roomescape.domain.member;

public class LoginResult {

    private final boolean success;
    private final String message;
    private final Member member;

    private LoginResult(boolean success, String message, Member member) {
        this.success = success;
        this.message = message;
        this.member = member;
    }

    public static LoginResult success(Member member) {
        return new LoginResult(true, "로그인 성공", member);
    }

    public static LoginResult failure(String message) {
        return new LoginResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Member getMember() {
        return member;
    }

}
