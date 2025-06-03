package roomescape.application.time;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.time.dto.CreateTimeRequest;
import roomescape.domain.common.exceptions.NotFoundException;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;
import roomescape.domain.time.ReservationTimes;
import roomescape.domain.time.TimeCreateResult;
import roomescape.domain.time.TimeDeleteResult;
import roomescape.domain.time.TimeDisplayInfo;
import roomescape.domain.time.TimeUsageChecker;

@Service
@Transactional
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final TimeUsageChecker timeUsageChecker;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  TimeUsageChecker timeUsageChecker) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.timeUsageChecker = timeUsageChecker;
    }

    // ✅ 예약 시간 생성 - 도메인 팩토리 활용
    public TimeCreateResult createTime(CreateTimeRequest request) {
        try {
            // 중복 체크
            if (reservationTimeRepository.existsByTimeString(request.getTimeString())) {
                return TimeCreateResult.failure("이미 존재하는 시간입니다.");
            }

            // 도메인 팩토리로 생성 (검증 포함)
            ReservationTime time = ReservationTime.create(request.getTimeString());

            ReservationTime saved = reservationTimeRepository.save(time);
            return TimeCreateResult.success(saved);

        } catch (IllegalArgumentException e) {
            return TimeCreateResult.failure(e.getMessage());
        }
    }

    // ✅ 예약 시간 삭제 - 도메인 객체 협력
    public TimeDeleteResult deleteTime(Long timeId) {
        ReservationTime time = findTimeById(timeId);

        // 도메인 객체가 삭제 가능 여부 판단
        TimeDeleteResult result = time.checkDeletable(timeUsageChecker);

        if (result.isSuccess()) {
            reservationTimeRepository.delete(time);
        }

        return result;
    }

    // ✅ 모든 예약 시간 조회
    @Transactional(readOnly = true)
    public List<ReservationTime> getAllTimes() {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        ReservationTimes reservationTimes = new ReservationTimes(times);

        // 도메인 컬렉션이 정렬
        return reservationTimes.sortByTime().toList();
    }

    // ✅ 예약 가능한 시간 조회 - 도메인 컬렉션 활용
    @Transactional(readOnly = true)
    public List<TimeDisplayInfo> getAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        ReservationTimes times = new ReservationTimes(allTimes);

        ReservationDate reservationDate = new ReservationDate(date);

        // 도메인 컬렉션이 가용성 체크와 표시 정보 생성
        return times.toDisplayInfos(reservationDate, themeId, timeUsageChecker);
    }

    private ReservationTime findTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다: " + timeId));
    }
}
