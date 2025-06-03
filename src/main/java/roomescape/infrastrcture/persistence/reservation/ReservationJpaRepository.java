package roomescape.infrastrcture.persistence.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT COUNT(r) > 0 FROM ReservationEntity r " +
            "WHERE r.date = :date AND r.reservationTime.startTime = :time AND r.theme.id = :themeId")
    boolean existsByDateAndReservationTimeAndTheme(LocalDate date, LocalTime startTime, Long themeId);

    @Query("SELECT COUNT(r) > 0 FROM ReservationEntity r " +
            "WHERE r.date = :date AND r.reservationTime.startTime = :time AND r.theme.id = :themeId")
    boolean existsByTimeAndDateAndThemeId(@Param("time") LocalTime time,
                                          @Param("date") LocalDate date,
                                          @Param("themeId") Long themeId);

    // 테마 사용 여부 체크용
    boolean existsByThemeId(Long themeId);

    int countByThemeId(Long themeId);

    @Query("SELECT COUNT(r) FROM ReservationEntity r " +
            "WHERE r.theme.id = :themeId AND r.date BETWEEN :startDate AND :endDate")
    int countByThemeIdAndDateBetween(@Param("themeId") Long themeId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    // 예약 시간 사용 여부 체크용
    boolean existsByReservationTimeId(Long reservationTimeId);

    // 회원별 예약 조회용
    List<ReservationEntity> findByMemberId(Long memberId);

    // 날짜 범위 조회용
    List<ReservationEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
}

