package roomescape.domain.member;

import java.util.Objects;

public class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    private final String value;

    public Password(String password) {
        validatePassword(password);
        this.value = password;
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("비밀번호는 %d자 이상 %d자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

    // 비밀번호 매칭 (암호화된 비밀번호와 비교)
    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.value);
    }

    public static Password encoded(String encodedPassword) {
        return new Password(encodedPassword);
    }

    // 암호화된 비밀번호 생성
    public static Password encode(String rawPassword, PasswordEncoder encoder) {
        String encodedPassword = encoder.encode(rawPassword);
        return new Password(encodedPassword);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Password password = (Password) obj;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


