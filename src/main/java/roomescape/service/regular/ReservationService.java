package roomescape.service.regular;

import java.util.List;
import java.util.Objects;
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
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.dto.response.reservation.MyReservationAndWaitingSortedResult;
import roomescape.dto.response.reservation.MyReservationRetrievalResponse;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.reservation.ReservationRetrievalResponse;
import roomescape.exception.ConflictException;
import roomescape.exception.ForbiddenException;
import roomescape.exception.RoomescapeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.WaitingRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository, final MemberRepository memberRepository,
                              final WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationPreservationResponse create(final RegularReservationPreservationRequest request,
                                                  final MemberPrinciple memberPrinciple) {
        Member member = getMember(memberPrinciple.memberId());
        ReservationTime reservationTime = getReservationTime(request.timeId());
        ReservationDate reservationDate = new ReservationDate(TimeUtils.parseLocalDate(request.date()));
        Theme theme = getTheme(request.themeId());
        validateReservationExists(reservationDate, reservationTime, theme);

        Reservation savedReservation = reservationRepository.save(
                Reservation.createReservation(reservationDate, reservationTime, theme, member));
        return ReservationPreservationResponse.from(savedReservation);
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
        List<MyReservationAndWaitingSortedResult> sortedResult = MyReservationAndWaitingSortedResult.of(reservations,
                waitingWithRanks);
        return sortedResult.stream()
                .map(MyReservationRetrievalResponse::from)
                .toList();
    }

    public void remove(final Long reservationId, final MemberPrinciple memberPrinciple) {
        Long memberId = memberPrinciple.memberId();
        if (reservationRepository.existsById(reservationId)) {
            Reservation reservation = checkOwner(reservationId, memberId);
            Slot slot = reservation.getSlot();
            if (waitingRepository.existsBySlot(slot)) {
                promotedWaiting(slot);
            }
        }
        reservationRepository.deleteById(reservationId);
    }

    private void promotedWaiting(final Slot slot) {
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankBySlot(slot);
        Waiting promotedWaiting = findPromotedWaiting(waitings);
        waitingRepository.deleteById(promotedWaiting.getId());
        reservationRepository.save(new Reservation(promotedWaiting.getSlot(), promotedWaiting.getMember()));
    }

    private Waiting findPromotedWaiting(final List<WaitingWithRank> waitings) {
        return waitings.stream()
                .filter(w -> w.getRank() == 1)
                .map(WaitingWithRank::getWaiting)
                .findFirst()
                .orElseThrow(() -> new RoomescapeException("Server internal exception"));
    }

    private Reservation getReservationIfIdExists(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RoomescapeException("Server internal exception"));
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
    }

    private Reservation checkOwner(final Long reservationId, final Long memberId) {
        Reservation reservation = getReservationIfIdExists(reservationId);
        if (!Objects.equals(reservation.getMember().getId(), memberId)) {
            throw new ForbiddenException("Reservation deletion is forbidden");
        }
        return reservation;
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
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }
}
