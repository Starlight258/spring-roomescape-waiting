package roomescape.domain.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Themes {

    private final List<Theme> themes;

    public Themes(List<Theme> themes) {
        this.themes = new ArrayList<>(themes);
    }

    // ✅ 인기 테마 추출 (지난 일주일 기준)
    public Themes getPopularThemes(int limit, ThemeUsageChecker usageChecker) {
        List<Theme> popularThemes = themes.stream()
                .map(theme -> new ThemeWithPopularity(theme, theme.calculatePopularity(usageChecker)))
                .sorted((a, b) -> Integer.compare(
                        b.getPopularityScore().getReservationCount(),
                        a.getPopularityScore().getReservationCount()
                ))
                .limit(limit)
                .map(ThemeWithPopularity::getTheme)
                .toList();

        return new Themes(popularThemes);
    }

    // ✅ 키워드 검색
    public Themes searchByKeyword(String keyword) {
        List<Theme> matchingThemes = themes.stream()
                .filter(theme -> theme.matchesSearchKeyword(keyword))
                .toList();
        return new Themes(matchingThemes);
    }

    // ✅ 삭제 가능한 테마들 필터링
    public Themes getDeletableThemes(ThemeUsageChecker usageChecker) {
        List<Theme> deletableThemes = themes.stream()
                .filter(theme -> theme.checkDeletable(usageChecker).isSuccess())
                .toList();
        return new Themes(deletableThemes);
    }

    // ✅ 이름 중복 체크
    public boolean hasThemeWithName(String name) {
        ThemeName themeName = new ThemeName(name);
        return themes.stream()
                .anyMatch(theme -> theme.getName().equals(themeName));
    }

    // ✅ 특정 테마 찾기
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId() != null && theme.getId().equals(id))
                .findFirst();
    }

    public List<Theme> toList() {
        return new ArrayList<>(themes);
    }

    public int size() {
        return themes.size();
    }

    public boolean isEmpty() {
        return themes.isEmpty();
    }

    // 내부 클래스 - 인기도와 함께 테마를 표현
    private static class ThemeWithPopularity {
        private final Theme theme;
        private final PopularityScore popularityScore;

        public ThemeWithPopularity(Theme theme, PopularityScore popularityScore) {
            this.theme = theme;
            this.popularityScore = popularityScore;
        }

        public Theme getTheme() {
            return theme;
        }

        public PopularityScore getPopularityScore() {
            return popularityScore;
        }
    }
}
