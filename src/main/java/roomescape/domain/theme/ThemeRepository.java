package roomescape.domain.theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);
    Optional<Theme> findById(Long id);
    List<Theme> findAll();
    void delete(Theme theme);
    boolean existsByName(String name);
}
