package roomescape.presentation.web.theme;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.theme.ThemeService;
import roomescape.domain.theme.Theme;
import roomescape.presentation.web.theme.dto.response.PopularThemeResponse;
import roomescape.presentation.web.theme.dto.response.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    // ✅ 모든 테마 조회
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {

        List<Theme> themes = themeService.getAllThemes();

        List<ThemeResponse> responses = themes.stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    // ✅ 인기 테마 조회 - Application Service가 도메인 컬렉션 활용
    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> getPopularThemes() {

        // Application Service가 Themes 컬렉션으로 인기 테마 계산
        List<Theme> popularThemes = themeService.getPopularThemes();

        List<PopularThemeResponse> responses = popularThemes.stream()
                .map(PopularThemeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}

