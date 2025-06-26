package roomescape.dto.response.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationRetrievalResponse(
        Long waitingId,
        String theme,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        String status
) {
    public static MyReservationRetrievalResponse from(final MyReservationAndWaitingSortedResult result) {
        return new MyReservationRetrievalResponse(
                result.waitingId(),
                result.theme(),
                result.date(),
                result.time(),
                result.status()
        );
    }
}
