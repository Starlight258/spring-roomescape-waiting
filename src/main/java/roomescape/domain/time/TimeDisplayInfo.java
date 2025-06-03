package roomescape.domain.time;

import java.util.Objects;

public class TimeDisplayInfo {

    private final Long timeId;
    private final String timeString;
    private final boolean available;
    private final String status;

    public TimeDisplayInfo(Long timeId, String timeString, boolean available, String status) {
        this.timeId = timeId;
        this.timeString = timeString;
        this.available = available;
        this.status = status;
    }

    public Long getTimeId() {
        return timeId;
    }

    public String getTimeString() {
        return timeString;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimeDisplayInfo that = (TimeDisplayInfo) obj;
        return Objects.equals(timeId, that.timeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeId);
    }
}
