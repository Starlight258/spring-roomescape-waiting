package roomescape.presentation.web.theme.dto.response;

import roomescape.domain.theme.Theme;

public class PopularThemeResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public PopularThemeResponse(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static PopularThemeResponse from(Theme theme) {
        return new PopularThemeResponse(
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

