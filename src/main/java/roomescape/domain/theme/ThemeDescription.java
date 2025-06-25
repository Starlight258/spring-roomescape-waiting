package roomescape.domain.theme;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.exception.BadRequestException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ThemeDescription {

    @Column(nullable = false)
    private String description;

    public ThemeDescription(final String description) {
        validate(description);
        this.description = description;
    }

    private void validate(final String description) {
        if (description.isBlank()) {
            throw new BadRequestException("Theme description is mandatory");
        }
    }
}
