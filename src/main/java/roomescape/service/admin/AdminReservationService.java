package roomescape.service.admin;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.request.reservation.AdminReservationPreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.WaitingRepository;
import roomescape.service.ReservationCreateHandler;
import roomescape.service.ReservationDeletionHandler;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;
    private final ReservationCreateHandler createHandler;
    private final ReservationDeletionHandler deleteHandler;

    public AdminReservationService(final ReservationRepository reservationRepository,
                                   final ReservationTimeRepository reservationTimeRepository,
                                   final ThemeRepository themeRepository, final MemberRepository memberRepository,
                                   final WaitingRepository waitingRepository,
                                   final ReservationCreateHandler createHandler,
                                   final ReservationDeletionHandler deleteHandler) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
        this.createHandler = createHandler;
        this.deleteHandler = deleteHandler;
    }

    public ReservationPreservationResponse create(final AdminReservationPreservationRequest request) {
        LocalDate date = TimeUtils.parseLocalDate(request.date());
        Reservation reservation = createHandler.create(date, request.timeId(), request.themeId(), request.memberId());
        return ReservationPreservationResponse.from(reservation);
    }

    public void remove(final Long reservationId) {
        deleteHandler.remove(reservationId, reservation -> {
        });
    }


}
