package roomescape.service.regular;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.slot.Slot;
import roomescape.domain.theme.Theme;
import roomescape.domain.waiting.WaitingWithRank;
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.dto.response.reservation.MyReservationRetrievalResponse;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.reservation.ReservationRetrievalResponse;
import roomescape.exception.ConflictException;
import roomescape.exception.ForbiddenException;
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
        return MyReservationRetrievalResponse.of(reservations, waitingWithRanks);
    }

    public void remove(final Long reservationId, final MemberPrinciple memberPrinciple) {
        Long memberId = memberPrinciple.memberId();
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return;
        }
        Reservation reservation = reservationOpt.get();
        validateOwner(reservation, memberId);

        Slot slot = reservation.getSlot();
        promotedWaiting(slot);
        reservationRepository.deleteById(reservationId);
    }

    private void validateOwner(final Reservation reservation, final Long memberId) {
        if (!Objects.equals(reservation.getMember().getId(), memberId)) {
            throw new ForbiddenException("Reservation deletion is forbidden");
        }
    }

    private void promotedWaiting(final Slot slot) {
        waitingRepository.findTopRankWaitingBySlot(slot.getTheme(), slot.getDate(),
                        slot.getTime())
                .ifPresent(waiting -> {
                    waitingRepository.deleteById(waiting.getId());
                    reservationRepository.save(new Reservation(waiting.getSlot(), waiting.getMember()));
                });
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다."));
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
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }
}
