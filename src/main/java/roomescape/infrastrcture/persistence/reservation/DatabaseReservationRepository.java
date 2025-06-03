package roomescape.infrastrcture.persistence.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;

@Repository
public class DatabaseReservationRepository implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;

    public DatabaseReservationRepository(ReservationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = ReservationEntity.from(reservation);
        ReservationEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ReservationEntity::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Reservation reservation) {
        ReservationEntity entity = ReservationEntity.from(reservation);
        jpaRepository.delete(entity);
    }
}
