package roomescape.dto.response.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationStatus;
import roomescape.domain.slot.Slot;
import roomescape.dto.request.waiting.WaitingWithRank;

public record MyReservationRetrievalResponse(
        Long waitingId,
        String theme,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        String status
) {
    public static List<MyReservationRetrievalResponse> of(final List<Reservation> reservations,
                                                          final List<WaitingWithRank> waitingWithRanks) {
        List<MyReservationRetrievalResponse> result = makeResult(reservations, waitingWithRanks);
        result.sort(Comparator.comparing(MyReservationRetrievalResponse::date)
                .thenComparing(MyReservationRetrievalResponse::time));
        return result;
    }

    private static List<MyReservationRetrievalResponse> makeResult(
            final List<Reservation> reservations, final List<WaitingWithRank> waitingWithRanks) {
        List<MyReservationRetrievalResponse> results = reservations.stream()
                .map(MyReservationRetrievalResponse::from)
                .collect(Collectors.toList());

        List<MyReservationRetrievalResponse> ranks = waitingWithRanks.stream()
                .map(MyReservationRetrievalResponse::from)
                .toList();

        results.addAll(ranks);
        return results;
    }

    private static MyReservationRetrievalResponse from(Reservation reservation) {
        Slot slot = reservation.getSlot();
        return new MyReservationRetrievalResponse(
                null,
                slot.getTheme().getName().getName(),
                slot.getDate().getDate(),
                slot.getTime().getStartAt(),
                ReservationStatus.RESERVED.getViewName()
        );
    }

    private static MyReservationRetrievalResponse from(WaitingWithRank waitingWithRank) {
        Slot slot = waitingWithRank.waiting().getSlot();
        return new MyReservationRetrievalResponse(
                waitingWithRank.waiting().getId(),
                slot.getTheme().getName().getName(),
                slot.getDate().getDate(),
                slot.getTime().getStartAt(),
                String.format(ReservationStatus.WAITING.getViewName(), waitingWithRank.rank())
        );
    }
}
