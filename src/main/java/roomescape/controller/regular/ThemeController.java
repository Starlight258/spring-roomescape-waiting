package roomescape.controller.regular;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireRole;
import roomescape.domain.member.MemberRole;
import roomescape.dto.request.theme.ThemePreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.theme.ThemePopularResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;
import roomescape.service.regular.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @RequireRole(role = MemberRole.ADMIN)
    @PostMapping
    public ResponseEntity<ThemeRetrievalResponse> create(
            final @RequestBody @Valid ThemePreservationRequest request) {
        ThemeRetrievalResponse response = themeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ThemeRetrievalResponse> findAll() {
        return themeService.findAll();
    }

    @GetMapping("/popular")
    public List<ThemePopularResponse> findTopPopular() {
        return themeService.findTopPopular();
    }

    @RequireRole(role = MemberRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationPreservationResponse> remove(final @PathVariable Long id) {
        themeService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
