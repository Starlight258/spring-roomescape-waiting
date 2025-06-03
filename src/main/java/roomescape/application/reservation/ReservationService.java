package roomescape.application.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.reservation.dto.CreateAdminReservationRequest;
import roomescape.application.reservation.dto.CreateReservationRequest;
import roomescape.application.reservation.dto.ReservationSearchRequest;
import roomescape.domain.common.exceptions.NotFoundException;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.reservation.CancelResult;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationResult;
import roomescape.domain.reservation.ReservationSearchCondition;
import roomescape.domain.reservation.Reservations;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationFactory reservationFactory;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationFactory reservationFactory) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationFactory = reservationFactory;
    }

    // ✅ 사용자 예약 생성 - 도메인 팩토리 활용
    public ReservationResult createReservation(CreateReservationRequest request, Member currentMember) {
        // 1. 필요한 도메인 객체들 조회
        Theme theme = findThemeById(request.getThemeId());
        ReservationTime time = findTimeById(request.getTimeId());

        // 2. 도메인 팩토리가 비즈니스 로직 담당
        ReservationResult result = reservationFactory.create(
                currentMember,
                request.getDate(),
                time,
                theme
        );

        // 3. 성공 시에만 저장 (Application 책임)
        if (result.isSuccess()) {
            reservationRepository.save(result.getReservation());
        }

        return result;
    }

    // ✅ 관리자 예약 생성 - 다른 회원 대신 예약
    public ReservationResult createAdminReservation(CreateAdminReservationRequest request) {
        // 1. 필요한 도메인 객체들 조회
        Member member = findMemberById(request.getMemberId());
        Theme theme = findThemeById(request.getThemeId());
        ReservationTime time = findTimeById(request.getTimeId());

        // 2. 관리자 예약은 이름을 회원 이름으로 자동 설정
        ReservationResult result = reservationFactory.create(
                member,
                request.getDate(),
                time,
                theme
        );

        if (result.isSuccess()) {
            reservationRepository.save(result.getReservation());
        }

        return result;
    }

    // ✅ 예약 취소 - 도메인 객체 협력
    public CancelResult cancelReservation(Long reservationId, Member currentMember) {
        Reservation reservation = findReservationById(reservationId);

        // 도메인 객체가 권한 체크와 취소 로직 담당
        if (!currentMember.canCancelReservation(reservation)) {
            return CancelResult.failure("예약을 취소할 권한이 없습니다.");
        }

        CancelResult result = reservation.cancel();

        if (result.isSuccess()) {
            reservationRepository.save(reservation);  // 상태 변경 저장
        }

        return result;
    }

    // ✅ 관리자 예약 취소 - 관리자는 모든 예약 취소 가능
    public CancelResult cancelAdminReservation(Long reservationId) {
        Reservation reservation = findReservationById(reservationId);

        CancelResult result = reservation.cancel();

        if (result.isSuccess()) {
            reservationRepository.save(reservation);
        }

        return result;
    }

    // ✅ 내 예약 목록 조회 - 도메인 컬렉션 활용
    @Transactional(readOnly = true)
    public List<Reservation> getMyReservations(Member currentMember) {
        List<Reservation> allReservations = reservationRepository.findAll();
        Reservations reservations = new Reservations(allReservations);

        return reservations.filterByMember(currentMember).toList();
    }

    // ✅ 모든 예약 조회
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // ✅ 조건별 예약 검색 - 도메인 컬렉션 활용
    @Transactional(readOnly = true)
    public List<Reservation> searchReservations(ReservationSearchRequest request) {
        List<Reservation> allReservations = reservationRepository.findAll();
        Reservations reservations = new Reservations(allReservations);

        ReservationSearchCondition condition = new ReservationSearchCondition(
                request.getThemeId(),
                request.getMemberId(),
                request.getDateFrom(),
                request.getDateTo()
        );

        return reservations.filterBy(condition).toList();
    }

    // Helper methods
    private Theme findThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("테마를 찾을 수 없습니다: " + themeId));
    }

    private ReservationTime findTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다: " + timeId));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다: " + memberId));
    }

    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다: " + reservationId));
    }
}

