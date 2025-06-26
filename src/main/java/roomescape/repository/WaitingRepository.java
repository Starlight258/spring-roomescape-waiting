package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.waiting.Waiting;
import roomescape.dto.request.waiting.WaitingWithRank;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT new roomescape.dto.request.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) + 1 " +
            "     FROM Waiting w2 " +
            "     WHERE w2.slot.theme = w.slot.theme " +
            "       AND w2.slot.date = w.slot.date " +
            "       AND w2.slot.time = w.slot.time " +
            "       AND w2.id < w.id)) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId);

    @Query("""
            SELECT w
            FROM Waiting w 
            WHERE w.slot.theme = :theme
                AND w.slot.date = :date
                AND w.slot.time = :time
                AND w.id = (
                    SELECT MIN(w2.id)
                    FROM Waiting w2
                    WHERE w2.slot.theme = :theme
                        AND w2.slot.date = :date
                        AND w2.slot.time = :time
                ) 
            """)
    Optional<Waiting> findTopRankWaitingBySlot(Theme theme, ReservationDate date, ReservationTime time);
}
