package roomescape.application.time.dto;

public class CreateTimeRequest {

    private final String timeString;

    public CreateTimeRequest(String timeString) {
        this.timeString = timeString;
    }

    public String getTimeString() { return timeString; }
}
