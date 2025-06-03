package roomescape.domain.member;

import java.util.Objects;

public class Email {

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final String value;

    public Email(String email) {
        validateEmail(email);
        this.value = email.toLowerCase().trim();
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public boolean isSame(Email other) {
        return this.value.equals(other.value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Email email = (Email) obj;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

