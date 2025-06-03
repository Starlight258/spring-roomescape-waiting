package roomescape.domain.member;

import roomescape.domain.common.Result;

public class SignUpResult implements Result {

    private final boolean success;
    private final String message;
    private final Member member;

    private SignUpResult(boolean success, String message, Member member) {
        this.success = success;
        this.message = message;
        this.member = member;
    }

    public static SignUpResult success(Member member) {
        return new SignUpResult(true, "회원가입이 완료되었습니다.", member);
    }

    public static SignUpResult failure(String message) {
        return new SignUpResult(false, message, null);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Member getMember() {
        if (!success) {
            throw new IllegalStateException("실패한 결과에서는 회원 객체를 가져올 수 없습니다.");
        }
        return member;
    }
}

