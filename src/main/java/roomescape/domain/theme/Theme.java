package roomescape.domain.theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Theme {

    private Long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final ThumbnailImage thumbnail;
    private LocalDateTime createdAt;

    // 생성자
    public Theme(ThemeName name, ThemeDescription description, ThumbnailImage thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.createdAt = LocalDateTime.now();
    }

    // 팩토리 메서드 - 테마 생성
    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(
                new ThemeName(name),
                new ThemeDescription(description),
                new ThumbnailImage(thumbnailUrl)
        );
    }


    public static Theme restore(final Long id, final ThemeName themeName, final ThemeDescription themeDescription,
                                final ThumbnailImage thumbnailImage, final LocalDateTime createdAt) {
        Theme theme = new Theme(themeName, themeDescription, thumbnailImage);
        theme.id = id;
        theme.createdAt = createdAt;
        return theme;
    }

    // ✅ Theme 자신의 행동: 삭제 가능 여부 확인
    public ThemeDeleteResult checkDeletable(ThemeUsageChecker usageChecker) {
        if (usageChecker.isInUse(this.id)) {
            return ThemeDeleteResult.failure("예약이 있는 테마는 삭제할 수 없습니다.");
        }
        return ThemeDeleteResult.success();
    }

    // ✅ Theme 자신의 행동: 인기도 계산
    public PopularityScore calculatePopularity(ThemeUsageChecker usageChecker) {
        LocalDate endDate = LocalDate.now().minusDays(1); // 어제까지
        LocalDate startDate = endDate.minusDays(6); // 지난 7일간

        int reservationCount = usageChecker.getReservationCountInPeriod(this.id, startDate, endDate);
        return new PopularityScore(reservationCount);
    }

    // ✅ Theme 자신의 행동: 정보 업데이트
    public Theme updateName(String newName) {
        ThemeName updatedName = new ThemeName(newName);
        return new Theme(updatedName, this.description, this.thumbnail);
    }

    public Theme updateDescription(String newDescription) {
        ThemeDescription updatedDescription = new ThemeDescription(newDescription);
        return new Theme(this.name, updatedDescription, this.thumbnail);
    }

    public Theme updateThumbnail(String newThumbnailUrl) {
        ThumbnailImage updatedThumbnail = new ThumbnailImage(newThumbnailUrl);
        return new Theme(this.name, this.description, updatedThumbnail);
    }

    // ✅ Theme 자신의 행동: 다른 테마와 비교
    public boolean hasSameName(Theme other) {
        return this.name.equals(other.name);
    }

    public boolean isSameTheme(Theme other) {
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    // ✅ Theme 자신의 행동: 검색 조건 매칭
    public boolean matchesSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();
        return name.getValue().toLowerCase().contains(lowerKeyword) ||
                description.getValue().toLowerCase().contains(lowerKeyword);
    }

    // ✅ Theme 자신의 행동: 유효성 검증
    public boolean isValid() {
        try {
            return name != null &&
                    description != null &&
                    thumbnail != null;
        } catch (Exception e) {
            return false;
        }
    }

    // getters
    public Long getId() {
        return id;
    }

    public ThemeName getName() {
        return name;
    }

    public ThemeDescription getDescription() {
        return description;
    }

    public ThumbnailImage getThumbnail() {
        return thumbnail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Theme theme = (Theme) obj;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Theme{id=%d, name='%s'}", id, name.getValue());
    }
}
