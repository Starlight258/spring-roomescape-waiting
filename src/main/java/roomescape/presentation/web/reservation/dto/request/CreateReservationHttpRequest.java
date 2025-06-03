package roomescape.presentation.web.reservation.dto.request;

import java.time.LocalDate;

public class CreateReservationHttpRequest {
    private String name;
    private LocalDate date;
    private Long timeId;
    private Long themeId;

    public CreateReservationHttpRequest() {
    }

    public CreateReservationHttpRequest(String name, LocalDate date, Long timeId, Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeId(Long timeId) {
        this.timeId = timeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
}


