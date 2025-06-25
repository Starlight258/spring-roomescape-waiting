package roomescape.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.theme.Theme;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsBySlotDateAndSlotTimeIdAndSlotThemeId(final ReservationDate date, final Long timeId, final Long themeId);

    boolean existsBySlotTimeId(Long timeId);

    boolean existsBySlotThemeId(Long themeId);

    Long countBySlotDateBetweenAndSlotTheme(ReservationDate startDate, ReservationDate endDate, Theme theme);

    List<Reservation> findByMemberId(Long memberId);
}
