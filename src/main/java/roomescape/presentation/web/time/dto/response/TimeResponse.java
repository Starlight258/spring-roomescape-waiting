package roomescape.presentation.web.time.dto.response;

import roomescape.domain.time.ReservationTime;

public class TimeResponse {

    private final Long id;
    private final String startAt;

    public TimeResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static TimeResponse from(ReservationTime reservationTime) {
        return new TimeResponse(
                reservationTime.getId(),
                reservationTime.getTimeString()
        );
    }

    public Long getId() { return id; }
    public String getStartAt() { return startAt; }
}
