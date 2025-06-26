package roomescape.dto.response.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationStatus;
import roomescape.domain.slot.Slot;
import roomescape.domain.waiting.WaitingWithRank;

// TODO: @JsonFormat
public record MyReservationAndWaitingSortedResult(
        Long waitingId,
        String theme,
        @JsonFormat(pattern = "HH:mm")
        LocalDate date,
        LocalTime time,
        String status
) {
    public static List<MyReservationAndWaitingSortedResult> of(final List<Reservation> reservations,
                                                               final List<WaitingWithRank> waitingWithRanks) {
        List<MyReservationAndWaitingSortedResult> result = makeResult(
                reservations, waitingWithRanks);
        return sort(result);
    }

    private static List<MyReservationAndWaitingSortedResult> makeResult(
            final List<Reservation> reservations, final List<WaitingWithRank> waitingWithRanks) {
        List<MyReservationAndWaitingSortedResult> results = reservations.stream()
                .map(MyReservationAndWaitingSortedResult::from)
                .collect(Collectors.toList());

        List<MyReservationAndWaitingSortedResult> ranks = waitingWithRanks.stream()
                .map(MyReservationAndWaitingSortedResult::from)
                .toList();

        results.addAll(ranks);
        return results;
    }

    // TODO : Comparator 리팩토링
    private static List<MyReservationAndWaitingSortedResult> sort(
            final List<MyReservationAndWaitingSortedResult> results) {
        results.sort((r1, r2) -> {
            LocalDateTime time1 = LocalDateTime.of(r1.date, r1.time);
            LocalDateTime time2 = LocalDateTime.of(r2.date, r2.time);
            if (time1.isBefore(time2)) {
                return -1;
            }
            return 1;
        });
        return results;
    }

    private static MyReservationAndWaitingSortedResult from(Reservation reservation) {
        Slot slot = reservation.getSlot();
        return new MyReservationAndWaitingSortedResult(
                null,
                slot.getTheme().getName().getName(),
                slot.getDate().getDate(),
                slot.getTime().getStartAt(),
                ReservationStatus.RESERVED.getViewName()
        );
    }

    private static MyReservationAndWaitingSortedResult from(WaitingWithRank waitingWithRank) {
        Slot slot = waitingWithRank.getWaiting().getSlot();
        return new MyReservationAndWaitingSortedResult(
                waitingWithRank.getWaiting().getId(),
                slot.getTheme().getName().getName(),
                slot.getDate().getDate(),
                slot.getTime().getStartAt(),
                String.format(ReservationStatus.WAITING.getViewName(), waitingWithRank.getRank())
        );
    }
}
