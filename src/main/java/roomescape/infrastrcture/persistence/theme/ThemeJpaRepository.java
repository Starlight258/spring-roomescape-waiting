package roomescape.infrastrcture.persistence.theme;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeJpaRepository extends JpaRepository<ThemeEntity, Long> {

    boolean existsByName(String name);

    Optional<ThemeEntity> findByName(String name);
}

