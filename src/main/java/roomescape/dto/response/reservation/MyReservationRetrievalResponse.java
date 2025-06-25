package roomescape.dto.response.reservation;

import roomescape.domain.reservation.Reservation;

public record MyReservationRetrievalResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {
    public static MyReservationRetrievalResponse from(Reservation reservation) {
        return new MyReservationRetrievalResponse(
                reservation.getId(),
                reservation.getTheme().getName().getName(),
                reservation.getDate().getDate().toString(),
                reservation.getTime().getStartAt().toString(),
                reservation.getStatus().getViewName()
        );
    }
}
