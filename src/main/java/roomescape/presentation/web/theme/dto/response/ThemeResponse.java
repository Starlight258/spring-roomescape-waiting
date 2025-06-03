package roomescape.presentation.web.theme.dto.response;

import roomescape.domain.theme.Theme;

public class ThemeResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeResponse(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().getValue(),
                theme.getDescription().getValue(),
                theme.getThumbnail().getUrl()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

