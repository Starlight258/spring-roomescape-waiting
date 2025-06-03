package roomescape.domain.time;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.ReservationDate;

public class ReservationTimes {

    private final List<ReservationTime> times;

    public ReservationTimes(List<ReservationTime> times) {
        this.times = new ArrayList<>(times);
    }

    // ✅ 시간순 정렬
    public ReservationTimes sortByTime() {
        List<ReservationTime> sorted = times.stream()
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .toList();
        return new ReservationTimes(sorted);
    }

    // ✅ 특정 날짜/테마에 예약 가능한 시간들만 필터링
    public ReservationTimes getAvailableTimesFor(ReservationDate date, Long themeId, TimeUsageChecker checker) {
        List<ReservationTime> available = times.stream()
                .filter(time -> time.isAvailableOn(date, themeId, checker))
                .toList();
        return new ReservationTimes(available);
    }

    // ✅ 삭제 가능한 시간들만 필터링
    public ReservationTimes getDeletableTimes(TimeUsageChecker checker) {
        List<ReservationTime> deletable = times.stream()
                .filter(time -> time.checkDeletable(checker).isSuccess())
                .toList();
        return new ReservationTimes(deletable);
    }

    // ✅ 특정 시간 찾기
    public Optional<ReservationTime> findByTimeString(String timeString) {
        return times.stream()
                .filter(time -> time.isSameTime(timeString))
                .findFirst();
    }

    // ✅ 시간 중복 체크
    public boolean hasTimeConflict(String newTimeString) {
        return findByTimeString(newTimeString).isPresent();
    }

    // ✅ 영업시간 내 시간들만 필터링
    public ReservationTimes getValidBusinessTimes() {
        List<ReservationTime> valid = times.stream()
                .filter(ReservationTime::isValidForReservation)
                .toList();
        return new ReservationTimes(valid);
    }

    // ✅ 표시용 정보 생성
    public List<TimeDisplayInfo> toDisplayInfos(ReservationDate date, Long themeId, TimeUsageChecker checker) {
        return sortByTime().times.stream()
                .map(time -> time.toDisplayInfo(date, themeId, checker))
                .toList();
    }

    // ✅ 특정 시간 범위 필터링
    public ReservationTimes getTimesBetween(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            List<ReservationTime> filtered = times.stream()
                    .filter(time -> !time.getStartTime().isBefore(start) &&
                            !time.getStartTime().isAfter(end))
                    .toList();

            return new ReservationTimes(filtered);
        } catch (Exception e) {
            return new ReservationTimes(Collections.emptyList());
        }
    }

    public List<ReservationTime> toList() {
        return new ArrayList<>(times);
    }

    public int size() {
        return times.size();
    }

    public boolean isEmpty() {
        return times.isEmpty();
    }
}
