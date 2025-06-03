package roomescape.domain.theme;

import java.util.Objects;

public class ThemeDescription {

    private static final int MAX_LENGTH = 500;

    private final String value;

    public ThemeDescription(String description) {
        validateDescription(description);
        this.value = description != null ? description.trim() : "";
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("테마 설명은 " + MAX_LENGTH + "글자 이하여야 합니다.");
        }
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThemeDescription that = (ThemeDescription) obj;
        return Objects.equals(value, that.value);
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
