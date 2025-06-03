package roomescape.application.theme.dto;

public class CreateThemeRequest {

    private final String name;
    private final String description;
    private final String thumbnailUrl;

    public CreateThemeRequest(String name, String description, String thumbnailUrl) {
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    // getters...
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}
