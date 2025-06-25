package roomescape.service.regular;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
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

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository, final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
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

    public void remove(final Long reservationId, final MemberPrinciple memberPrinciple) {
        Long memberId = memberPrinciple.memberId();
        if (reservationRepository.existsById(reservationId)) {
            Reservation reservation = getReservation(reservationId);
            if (!Objects.equals(reservation.getMember().getId(), memberId)) {
                throw new ForbiddenException("Reservation deletion is forbidden");
            }
        }
        reservationRepository.deleteById(reservationId);
    }

    private Reservation getReservation(final Long reservationId) {
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

    public List<MyReservationRetrievalResponse> findMyReservations(final MemberPrinciple memberPrinciple) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberPrinciple.memberId());
        return reservations.stream()
                .map(MyReservationRetrievalResponse::from)
                .toList();
    }
}
