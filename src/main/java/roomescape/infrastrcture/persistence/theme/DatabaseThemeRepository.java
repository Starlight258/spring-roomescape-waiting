package roomescape.infrastrcture.persistence.theme;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

@Repository
public class DatabaseThemeRepository implements ThemeRepository {

    private final ThemeJpaRepository jpaRepository;

    public DatabaseThemeRepository(ThemeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Theme save(Theme theme) {
        ThemeEntity entity = ThemeEntity.from(theme);
        ThemeEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ThemeEntity::toDomain);
    }

    @Override
    public List<Theme> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ThemeEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Theme theme) {
        ThemeEntity entity = ThemeEntity.from(theme);
        jpaRepository.delete(entity);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

}
