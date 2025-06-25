package roomescape.service.admin;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.slot.Slot;
import roomescape.domain.theme.Theme;
import roomescape.domain.waiting.Waiting;
import roomescape.domain.waiting.WaitingWithRank;
import roomescape.dto.request.reservation.AdminReservationPreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.RoomescapeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.WaitingRepository;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public AdminReservationService(final ReservationRepository reservationRepository,
                                   final ReservationTimeRepository reservationTimeRepository,
                                   final ThemeRepository themeRepository, final MemberRepository memberRepository,
                                   final WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationPreservationResponse create(final AdminReservationPreservationRequest request) {
        Member member = getMember(request.memberId());
        ReservationDate reservationDate = new ReservationDate(TimeUtils.parseLocalDate(request.date()));
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());
        validateReservationExists(reservationDate, reservationTime, theme);

        Reservation savedReservation = reservationRepository.save(
                Reservation.createReservation(reservationDate, reservationTime, theme, member));
        return ReservationPreservationResponse.from(savedReservation);
    }

    public void remove(final Long reservationId) {
        if (reservationRepository.existsById(reservationId)) {
            Reservation reservation = getReservation(reservationId);
            Slot slot = reservation.getSlot();
            if (waitingRepository.existsBySlot(slot)) {
                promotedWaiting(slot);
            }
        }
        reservationRepository.deleteById(reservationId);
    }

    private void promotedWaiting(final Slot slot) {
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankBySlot(slot);
        Waiting promotedWaiting = findWaiting(waitings);
        waitingRepository.deleteById(promotedWaiting.getId());
        reservationRepository.save(new Reservation(promotedWaiting.getSlot(), promotedWaiting.getMember()));
    }

    private Waiting findWaiting(final List<WaitingWithRank> waitings) {
        return waitings.stream()
                .filter(w -> w.getRank() == 1)
                .map(WaitingWithRank::getWaiting)
                .findFirst()
                .orElseThrow(() -> new RoomescapeException("Server internal exception"));
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    private Reservation getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
    }

    private void validateReservationExists(final ReservationDate reservationDate,
                                           final ReservationTime reservationTime,
                                           final Theme theme) {
        if (reservationRepository.existsBySlotDateAndSlotTimeIdAndSlotThemeId(reservationDate, reservationTime.getId(),
                theme.getId())) {
            throw new ConflictException("Reservation is already exists");
        }
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BadRequestException("멤버가 존재하지 않습니다."));
    }
}
