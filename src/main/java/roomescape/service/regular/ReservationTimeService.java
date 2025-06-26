package roomescape.service.regular;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.TimeUtils;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.request.reservationtime.ReservationTimePreservationRequest;
import roomescape.dto.response.reservationtime.ReservationTimeAvailableResponse;
import roomescape.dto.response.reservationtime.ReservationTimePreservationResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimePreservationResponse create(final ReservationTimePreservationRequest request) {
        ReservationTime reservationTime = new ReservationTime(TimeUtils.parseLocalTime(request.startAt()));
        validateReservationTimeExists(reservationTime);
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimePreservationResponse.from(savedReservationTime);
    }

    public List<ReservationTimeRetrievalResponse> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeRetrievalResponse::from)
                .toList();
    }

    public List<ReservationTimeAvailableResponse> findAllAvailable(final String date, final Long themeId) {
        ReservationDate parsedDate = new ReservationDate(TimeUtils.parseLocalDate(date));
        List<ReservationTime> times = reservationTimeRepository.findAll();
        return times.stream()
                .map(time -> makeReservationTimeAvailableResponse(parsedDate, time, themeId))
                .toList();
    }

    private ReservationTimeAvailableResponse makeReservationTimeAvailableResponse(ReservationDate date,
                                                                                  ReservationTime time, Long themeId) {
        Long timeId = time.getId();
        LocalTime startAt = time.getStartAt();
        if (reservationRepository.existsBySlotDateAndSlotTimeIdAndSlotThemeId(date, timeId, themeId)) {
            return new ReservationTimeAvailableResponse(startAt, timeId, true);
        }
        return new ReservationTimeAvailableResponse(startAt, timeId, false);
    }

    public void remove(final Long id) {
        validateReservationNotExists(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateReservationTimeExists(final ReservationTime reservationTime) {
        if (reservationTimeRepository.existsByStartAt(reservationTime.getStartAt())) {
            throw new ConflictException("Reservation time is already exists");
        }
    }

    private void validateReservationNotExists(final Long id) {
        if (reservationRepository.existsBySlotTimeId(id)) {
            throw new BadRequestException("The reservation time is referenced by reservation");
        }
    }
}
