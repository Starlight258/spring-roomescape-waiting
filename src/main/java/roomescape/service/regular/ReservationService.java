package roomescape.service.regular;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.request.waiting.WaitingWithRank;
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.dto.response.reservation.MyReservationRetrievalResponse;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.reservation.ReservationRetrievalResponse;
import roomescape.exception.ForbiddenException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.WaitingRepository;
import roomescape.service.ReservationCreateHandler;
import roomescape.service.ReservationDeletionHandler;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final ReservationCreateHandler createHandler;
    private final ReservationDeletionHandler reservationDeletionHandler;

    public ReservationService(final ReservationRepository reservationRepository,
                              final WaitingRepository waitingRepository,
                              final ReservationCreateHandler createHandler,
                              final ReservationDeletionHandler reservationDeletionHandler
    ) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.createHandler = createHandler;
        this.reservationDeletionHandler = reservationDeletionHandler;
    }

    public ReservationPreservationResponse create(final RegularReservationPreservationRequest request,
                                                  final MemberPrinciple memberPrinciple) {
        LocalDate date = TimeUtils.parseLocalDate(request.date());
        Reservation reservation = createHandler.create(date, request.timeId(), request.themeId(),
                memberPrinciple.memberId());
        return ReservationPreservationResponse.from(reservation);
    }

    public List<ReservationRetrievalResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationRetrievalResponse::from)
                .toList();
    }

    public List<MyReservationRetrievalResponse> findMyReservations(final MemberPrinciple memberPrinciple) {
        Long memberId = memberPrinciple.memberId();
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        List<WaitingWithRank> waitingWithRanks = waitingRepository.findWaitingsWithRankByMemberId(memberId);
        return MyReservationRetrievalResponse.of(reservations, waitingWithRanks);
    }

    public void remove(final Long reservationId, final MemberPrinciple memberPrinciple) {
        reservationDeletionHandler.remove(reservationId, r -> {
            if (!Objects.equals(r.getMember().getId(), memberPrinciple.memberId())) {
                throw new ForbiddenException("Reservation deletion is forbidden");
            }
        });
    }
}
