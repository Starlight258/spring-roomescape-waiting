package roomescape.service.regular;

import java.util.Objects;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.waiting.Waiting;
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.waiting.WaitingPreservationRequest;
import roomescape.dto.response.waiting.WaitingPreservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ForbiddenException;
import roomescape.exception.RoomescapeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.WaitingRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(final WaitingRepository waitingRepository, final ReservationRepository reservationRepository,
                          final ReservationTimeRepository reservationTimeRepository,
                          final ThemeRepository themeRepository, final MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingPreservationResponse create(final WaitingPreservationRequest request,
                                              final MemberPrinciple memberPrinciple) {
        Member member = getMember(memberPrinciple.memberId());
        ReservationTime reservationTime = getReservationTime(request.timeId());
        ReservationDate reservationDate = new ReservationDate(TimeUtils.parseLocalDate(request.date()));
        Theme theme = getTheme(request.themeId());
        Reservation reservation = getReservation(reservationDate, reservationTime, theme);
        validateAlreadyReserved(memberPrinciple, reservation);

        Waiting waiting = waitingRepository.save(
                Waiting.createWaiting(reservationDate, reservationTime, theme, member));
        return WaitingPreservationResponse.from(waiting);
    }

    public void remove(final Long waitingId, final MemberPrinciple memberPrinciple) {
        Long memberId = memberPrinciple.memberId();
        if (waitingRepository.existsById(waitingId)) {
            Waiting waiting = getWaitingIfIdExists(waitingId);
            if (!Objects.equals(waiting.getMember().getId(), memberId)) {
                throw new ForbiddenException("Reservation deletion is forbidden");
            }
        }
        waitingRepository.deleteById(waitingId);
    }

    private void validateAlreadyReserved(final MemberPrinciple memberPrinciple, final Reservation reservation) {
        if (reservation.getMember().getId().equals(memberPrinciple.memberId())) {
            throw new BadRequestException("The member is already reserved");
        }
    }

    private Waiting getWaitingIfIdExists(final Long id) {
        return waitingRepository.findById(id)
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

    private Reservation getReservation(final ReservationDate reservationDate,
                                       final ReservationTime reservationTime,
                                       final Theme theme) {
        return reservationRepository.findBySlotDateAndSlotTimeIdAndSlotThemeId(
                reservationDate,
                reservationTime.getId(),
                theme.getId()
        ).orElseThrow(() -> new BadRequestException("Reservation does not exist"));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }
}
