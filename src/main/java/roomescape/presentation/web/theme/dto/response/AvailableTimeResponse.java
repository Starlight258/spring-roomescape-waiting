package roomescape.presentation.web.theme.dto.response;

import roomescape.domain.time.TimeDisplayInfo;

public class AvailableTimeResponse {

    private final Long timeId;
    private final String startAt;
    private final boolean alreadyBooked;

    public AvailableTimeResponse(final Long timeId, final String startAt, final boolean alreadyBooked) {
        this.timeId = timeId;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public static AvailableTimeResponse from(TimeDisplayInfo displayInfo) {
        return new AvailableTimeResponse(
                displayInfo.getTimeId(),
                displayInfo.getTimeString(),
                !displayInfo.isAvailable()
        );
    }

    public Long getTimeId() {
        return timeId;
    }

    public String getStartAt() {
        return startAt;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}

