package roomescape.infrastrcture.persistence.time;

import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTimeJpaRepository extends JpaRepository<ReservationTimeEntity, Long> {

    boolean existsByStartTime(LocalTime startTime);

    @Query("SELECT COUNT(rt) > 0 FROM ReservationTimeEntity rt WHERE rt.startTime = :time")
    boolean existsByTimeString(@Param("time") LocalTime time);

    List<ReservationTimeEntity> findAllByOrderByStartTimeAsc();
}
