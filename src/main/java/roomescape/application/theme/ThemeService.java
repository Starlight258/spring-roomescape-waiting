package roomescape.application.theme;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.theme.dto.CreateThemeRequest;
import roomescape.domain.common.exceptions.DuplicatedException;
import roomescape.domain.common.exceptions.NotFoundException;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDeleteResult;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.theme.ThemeUsageChecker;
import roomescape.domain.theme.Themes;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeUsageChecker themeUsageChecker;

    public ThemeService(ThemeRepository themeRepository, ThemeUsageChecker themeUsageChecker) {
        this.themeRepository = themeRepository;
        this.themeUsageChecker = themeUsageChecker;
    }

    // ✅ 테마 생성 - 도메인 팩토리 활용
    public Theme createTheme(CreateThemeRequest request) {
        // 중복 체크
        if (themeRepository.existsByName(request.getName())) {
            throw new DuplicatedException("이미 존재하는 테마 이름입니다.");
        }

        // 도메인 팩토리로 생성 (검증 포함)
        Theme theme = Theme.create(
                request.getName(),
                request.getDescription(),
                request.getThumbnailUrl()
        );

        return themeRepository.save(theme);
    }

    // ✅ 테마 삭제 - 도메인 객체 협력
    public ThemeDeleteResult deleteTheme(Long themeId) {
        Theme theme = findThemeById(themeId);

        // 도메인 객체가 삭제 가능 여부 판단
        ThemeDeleteResult result = theme.checkDeletable(themeUsageChecker);

        if (result.isSuccess()) {
            themeRepository.delete(theme);
        }

        return result;
    }

    // ✅ 모든 테마 조회
    @Transactional(readOnly = true)
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    // ✅ 인기 테마 조회 - 도메인 컬렉션 활용
    @Transactional(readOnly = true)
    public List<Theme> getPopularThemes() {
        List<Theme> allThemes = themeRepository.findAll();
        Themes themes = new Themes(allThemes);

        // 도메인 컬렉션이 인기 테마 계산
        Themes popularThemes = themes.getPopularThemes(10, themeUsageChecker);

        return popularThemes.toList();
    }

    private Theme findThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("테마를 찾을 수 없습니다: " + themeId));
    }
}
