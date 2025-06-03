package roomescape.domain.theme;

import java.util.Objects;

public class ThemeName {

    private static final int MAX_LENGTH = 50;

    private final String value;

    public ThemeName(String name) {
        validateName(name);
        this.value = name.trim();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }

        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("테마 이름은 " + MAX_LENGTH + "글자 이하여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThemeName themeName = (ThemeName) obj;
        return Objects.equals(value, themeName.value);
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
