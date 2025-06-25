package roomescape.dto.response.reservation;

public record MyReservationRetrievalResponse(
        Long waitingId,
        String theme,
        String date,
        String time,
        String status
) {
    public static MyReservationRetrievalResponse from(final MyReservationAndWaitingSortedResult result) {
        return new MyReservationRetrievalResponse(result.waitingId(), result.theme(), result.date().toString(), result.time().toString(),
                result.status());
    }
}
