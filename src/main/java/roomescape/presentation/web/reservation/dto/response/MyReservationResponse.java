package roomescape.presentation.web.reservation.dto.response;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;

public class MyReservationResponse {

    private final Long reservationId;
    private final String theme;
    private final LocalDate date;
    private final String time;
    private final String status;

    public MyReservationResponse(final Long reservationId, final String theme, final LocalDate date, final String time,
                                 final String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName().getValue(),
                reservation.getDate().getDate(),
                reservation.getTime().getTimeString(),
                reservation.getStatus().name()
        );
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
