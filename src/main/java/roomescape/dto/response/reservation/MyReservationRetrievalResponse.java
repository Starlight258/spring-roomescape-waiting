package roomescape.dto.response.reservation;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationStatus;
import roomescape.domain.slot.Slot;

public record MyReservationRetrievalResponse(
        String theme,
        String date,
        String time,
        String status
) {
    public static MyReservationRetrievalResponse from(Reservation reservation) {
        Slot slot = reservation.getSlot();
        return new MyReservationRetrievalResponse(
                slot.getTheme().getName().getName(),
                slot.getDate().getDate().toString(),
                slot.getTime().getStartAt().toString(),
                ReservationStatus.RESERVED.getViewName()
        );
    }
}
