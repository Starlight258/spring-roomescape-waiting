package roomescape.presentation.web.theme;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.theme.ThemeService;
import roomescape.application.theme.dto.CreateThemeRequest;
import roomescape.domain.common.exceptions.DuplicatedException;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDeleteResult;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.web.common.response.ErrorResponse;
import roomescape.presentation.web.theme.dto.request.CreateThemeHttpRequest;
import roomescape.presentation.web.theme.dto.response.ThemeResponse;

@RestController
@RequestMapping("/admin/themes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    // ✅ 테마 생성 - Application Service가 도메인 팩토리 활용
    @PostMapping
    public ResponseEntity<?> createTheme(@RequestBody CreateThemeHttpRequest request) {

        try {
            CreateThemeRequest serviceRequest = new CreateThemeRequest(
                    request.getName(),
                    request.getDescription(),
                    request.getThumbnailUrl()
            );

            // Application Service가 도메인 팩토리로 테마 생성
            Theme theme = themeService.createTheme(serviceRequest);

            ThemeResponse response = ThemeResponse.from(theme);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(URI.create("/admin/themes/" + theme.getId()))
                    .body(response);

        } catch (DuplicatedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // ✅ 테마 삭제 - Application Service가 도메인 객체 협력 조율
    @DeleteMapping("/{themeId}")
    public ResponseEntity<?> deleteTheme(@PathVariable Long themeId) {

        // Application Service가 Theme.checkDeletable() 활용
        ThemeDeleteResult result = themeService.deleteTheme(themeId);

        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(result.getMessage()));
        }
    }
}

