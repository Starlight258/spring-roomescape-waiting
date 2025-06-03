package roomescape.infrastrcture.persistence.time;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;

@Repository
public class DatabaseReservationTimeRepository implements ReservationTimeRepository {

    private final ReservationTimeJpaRepository jpaRepository;

    public DatabaseReservationTimeRepository(ReservationTimeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTimeEntity entity = ReservationTimeEntity.from(reservationTime);
        ReservationTimeEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ReservationTimeEntity::toDomain);
    }

    @Override
    public List<ReservationTime> findAll() {
        return jpaRepository.findAllByOrderByStartTimeAsc()
                .stream()
                .map(ReservationTimeEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(ReservationTime reservationTime) {
        ReservationTimeEntity entity = ReservationTimeEntity.from(reservationTime);
        jpaRepository.delete(entity);
    }

    @Override
    public boolean existsByTimeString(String timeString) {
        try {
            LocalTime time = LocalTime.parse(timeString);
            return jpaRepository.existsByStartTime(time);
        } catch (Exception e) {
            return false;
        }
    }
}
