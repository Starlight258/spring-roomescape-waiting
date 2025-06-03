package roomescape.domain.member;

import java.util.Objects;

public class MemberName {

    private static final int MAX_LENGTH = 10;

    private final String value;

    public MemberName(String name) {
        validateName(name);
        this.value = name.trim();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("이름은 " + MAX_LENGTH + "글자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MemberName memberName = (MemberName) obj;
        return Objects.equals(value, memberName.value);
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

