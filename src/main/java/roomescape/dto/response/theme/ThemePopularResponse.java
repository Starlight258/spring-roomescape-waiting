package roomescape.dto.response.theme;

import roomescape.domain.theme.Theme;

public record ThemePopularResponse(String name, String description, String thumbnail) {

    public static ThemePopularResponse from(Theme theme) {
        return new ThemePopularResponse(theme.getName().getName(),
                theme.getDescription().getDescription(),
                theme.getThumbnail().getThumbnail());
    }
}
