package roomescape.presentation.web.time.dto.response;

import roomescape.domain.time.ReservationTime;

public class TimeInfo {

    private final Long id;
    private final String startAt;

    public TimeInfo(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static TimeInfo from(ReservationTime reservationTime) {
        return new TimeInfo(
                reservationTime.getId(),
                reservationTime.getTimeString()
        );
    }

    public Long getId() { return id; }
    public String getStartAt() { return startAt; }
}
