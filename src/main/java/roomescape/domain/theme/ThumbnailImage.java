package roomescape.domain.theme;

import java.util.Objects;
import java.util.regex.Pattern;

public class ThumbnailImage {

    private static final String DEFAULT_THUMBNAIL = "/images/default-theme.png";
    private static final Pattern URL_PATTERN =
            Pattern.compile("^(https?://|/)[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+$");

    private final String url;

    public ThumbnailImage(String url) {
        this.url = validateAndNormalize(url);
    }

    private String validateAndNormalize(String url) {
        if (url == null || url.trim().isEmpty()) {
            return DEFAULT_THUMBNAIL;
        }

        String normalizedUrl = url.trim();
        if (!URL_PATTERN.matcher(normalizedUrl).matches()) {
            throw new IllegalArgumentException("올바른 이미지 URL 형식이 아닙니다.");
        }

        return normalizedUrl;
    }

    public boolean isDefault() {
        return DEFAULT_THUMBNAIL.equals(url);
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThumbnailImage that = (ThumbnailImage) obj;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return url;
    }
}
