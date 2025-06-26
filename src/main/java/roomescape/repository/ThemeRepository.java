package roomescape.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeName;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    boolean existsByName(@NotBlank ThemeName name);
}
