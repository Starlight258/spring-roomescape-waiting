package roomescape.service.admin;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.waiting.Waiting;
import roomescape.dto.response.waiting.WaitingRetrievalResponse;
import roomescape.repository.WaitingRepository;

@Service
public class AdminWaitingService {

    private final WaitingRepository waitingRepository;

    public AdminWaitingService(final WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    public List<WaitingRetrievalResponse> findAll() {
        List<Waiting> waitings = waitingRepository.findAll();
        return waitings.stream()
                .map(WaitingRetrievalResponse::from)
                .toList();
    }

    public void remove(final Long waitingId) {
        waitingRepository.deleteById(waitingId);
    }
}
