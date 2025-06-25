package roomescape.dto.response.reservationtime;

import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimePreservationResponse(Long id, String startAt) {

    public static ReservationTimePreservationResponse from(final ReservationTime reservationTime) {
        return new ReservationTimePreservationResponse(reservationTime.getId(),
                reservationTime.getStartAt().toString());
    }
}
