package roomescape.dto.request.waiting;

import roomescape.domain.waiting.Waiting;

public record WaitingWithRank(
        Waiting waiting,
        Long rank
) {
}

