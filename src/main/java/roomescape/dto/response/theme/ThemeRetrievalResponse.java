package roomescape.dto.response.theme;

import roomescape.domain.theme.Theme;

public record ThemeRetrievalResponse(Long id, String name, String description, String thumbnail) {

    public static ThemeRetrievalResponse from(Theme theme) {
        return new ThemeRetrievalResponse(theme.getId(), theme.getName().getName(),
                theme.getDescription().getDescription(),
                theme.getThumbnail().getThumbnail());
    }
}
