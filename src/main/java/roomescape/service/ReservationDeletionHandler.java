package roomescape.service;

import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.slot.Slot;
import roomescape.repository.ReservationRepository;
import roomescape.repository.WaitingRepository;

@Service
public class ReservationDeletionHandler {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationDeletionHandler(final ReservationRepository reservationRepository,
                                      final WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public void remove(final Long reservationId, Consumer<Reservation> preCheck) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return;
        }
        Reservation reservation = reservationOpt.get();
        preCheck.accept(reservation);

        Slot slot = reservation.getSlot();
        promotedWaiting(slot);
        reservationRepository.deleteById(reservationId);
    }

    private void promotedWaiting(final Slot slot) {
        waitingRepository.findTopRankWaitingBySlot(slot.getTheme(), slot.getDate(),
                        slot.getTime())
                .ifPresent(waiting -> {
                    waitingRepository.deleteById(waiting.getId());
                    reservationRepository.save(new Reservation(waiting.getSlot(), waiting.getMember()));
                });
    }
}
