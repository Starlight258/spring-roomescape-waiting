package roomescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.domain.waiting.Waiting;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
