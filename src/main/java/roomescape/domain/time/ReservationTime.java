package roomescape.domain.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;
import roomescape.domain.reservation.ReservationDate;

public class ReservationTime {

    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    private static final LocalTime BUSINESS_OPEN = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_CLOSE = LocalTime.of(22, 0);

    private Long id;
    private final LocalTime startTime;
    private LocalDateTime createdAt;

    // 생성자
    public ReservationTime(LocalTime startTime) {
        validateBusinessRules(startTime);
        this.startTime = startTime;
        this.createdAt = LocalDateTime.now();
    }

    // 팩토리 메서드
    public static ReservationTime create(String timeString) {
        LocalTime time = parseAndValidateTime(timeString);
        return new ReservationTime(time);
    }

    public static ReservationTime create(LocalTime time) {
        return new ReservationTime(time);
    }

    // ✅ 시간 형식 검증 및 파싱
    private static LocalTime parseAndValidateTime(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            throw new IllegalArgumentException("시간은 필수입니다.");
        }

        if (!TIME_PATTERN.matcher(timeString).matches()) {
            throw new IllegalArgumentException("시간은 HH:mm 형식이어야 합니다.");
        }

        return LocalTime.parse(timeString);
    }

    public static ReservationTime restore(final Long id, final LocalTime startTime, final LocalDateTime createdAt) {
        ReservationTime reservationTime = new ReservationTime(startTime);
        reservationTime.id = id;  // ID 설정 (리플렉션이나 별도 설정 필요)
        reservationTime.createdAt = createdAt;  // 생성일 복원
        return reservationTime;
    }

    // ✅ 비즈니스 규칙 검증
    private void validateBusinessRules(LocalTime time) {
        // 영업 시간 체크
        if (time.isBefore(BUSINESS_OPEN) || time.isAfter(BUSINESS_CLOSE)) {
            throw new IllegalArgumentException("영업 시간(09:00~22:00) 내에서만 예약 가능합니다.");
        }

        // 정시/30분 단위만 허용
        if (time.getMinute() != 0 && time.getMinute() != 30) {
            throw new IllegalArgumentException("예약 시간은 정시 또는 30분 단위만 가능합니다.");
        }
    }

    public boolean isPastTime(ReservationDate reservationDate) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate.getDate(), this.startTime);
        return reservationDateTime.isBefore(LocalDateTime.now());
    }


    // ✅ ReservationTime 자신의 행동: 특정 날짜에 예약 가능한지 확인
    public boolean isAvailableOn(ReservationDate reservationDate, Long themeId, TimeUsageChecker checker) {
        // 과거 시간은 예약 불가
        if (isPastTime(reservationDate)) {
            return false;
        }

        // 이미 예약이 있는지 확인
        return !checker.hasReservationAt(this.startTime, reservationDate, themeId);
    }

    // ✅ ReservationTime 자신의 행동: 삭제 가능 여부 확인
    public TimeDeleteResult checkDeletable(TimeUsageChecker usageChecker) {
        if (usageChecker.isTimeInUse(this.id)) {
            return TimeDeleteResult.failure("해당 시간에 예약이 있어 삭제할 수 없습니다.");
        }

        return TimeDeleteResult.success();
    }

    // ✅ ReservationTime 자신의 행동: 시간 비교
    public boolean isBefore(ReservationTime other) {
        return this.startTime.isBefore(other.startTime);
    }

    public boolean isAfter(ReservationTime other) {
        return this.startTime.isAfter(other.startTime);
    }

    public boolean isSameTime(ReservationTime other) {
        return this.startTime.equals(other.startTime);
    }

    public boolean isSameTime(String timeString) {
        try {
            LocalTime otherTime = LocalTime.parse(timeString);
            return this.startTime.equals(otherTime);
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ ReservationTime 자신의 행동: 시간 계산
    public Duration getDurationUntil(ReservationTime other) {
        return Duration.between(this.startTime, other.startTime);
    }

    public ReservationTime getNextTimeSlot() {
        LocalTime nextTime = startTime.plusMinutes(30);
        if (nextTime.isAfter(BUSINESS_CLOSE)) {
            throw new IllegalStateException("다음 시간대가 영업 시간을 벗어납니다.");
        }
        return new ReservationTime(nextTime);
    }

    // ✅ ReservationTime 자신의 행동: 동일성 확인
    public boolean isSameReservationTime(ReservationTime other) {
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    // ✅ ReservationTime 자신의 행동: 비즈니스 규칙 검증
    public boolean isValidForReservation() {
        // 영업 시간 내인지
        boolean withinBusinessHours = !startTime.isBefore(BUSINESS_OPEN) &&
                !startTime.isAfter(BUSINESS_CLOSE);

        // 정해진 시간 단위인지
        boolean validInterval = (startTime.getMinute() == 0 || startTime.getMinute() == 30);

        return withinBusinessHours && validInterval;
    }

    // ✅ ReservationTime 자신의 행동: 표시용 정보 생성
    public TimeDisplayInfo toDisplayInfo(ReservationDate date, Long themeId, TimeUsageChecker checker) {
        boolean available = isAvailableOn(date, themeId, checker);
        String status;

        if (isPastTime(date)) {
            status = "지난 시간";
        } else if (available) {
            status = "예약 가능";
        } else {
            status = "예약 불가";
        }

        return new TimeDisplayInfo(this.id, getTimeString(), available, status);
    }

    // ✅ ReservationTime 자신의 행동: 시간 포맷팅
    public String getTimeString() {
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    // getters
    public Long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("ReservationTime{id=%d, time='%s'}", id, getTimeString());
    }
}
