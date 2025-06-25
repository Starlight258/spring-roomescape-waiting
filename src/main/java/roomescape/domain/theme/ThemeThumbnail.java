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
public class ThemeThumbnail {

    @Column(nullable = false)
    private String thumbnail;

    public ThemeThumbnail(final String thumbnail) {
        validate(thumbnail);
        this.thumbnail = thumbnail;
    }

    private void validate(final String thumbnail) {
        if (thumbnail.isBlank()) {
            throw new BadRequestException("Theme thumbnail is mandatory");
        }
    }
}
