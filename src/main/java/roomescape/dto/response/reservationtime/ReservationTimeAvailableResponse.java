package roomescape.dto.response.reservationtime;

public record ReservationTimeAvailableResponse(String startAt, Long timeId, boolean alreadyBooked) {
}
