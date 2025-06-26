package roomescape.domain.theme;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import roomescape.domain.reservation.ReservationDate;
import roomescape.service.regular.ReservationDateCounterByTheme;

public class PopularTheme {

    private final Map<Theme, Long> counts = new LinkedHashMap<>();

    public PopularTheme(final LocalDate nowDate, final List<Theme> themes,
                        final ReservationDateCounterByTheme counter) {
        ReservationDate startDate = new ReservationDate(nowDate.minusDays(7));
        ReservationDate endDate = new ReservationDate(nowDate.minusDays(1));
        for (Theme theme : themes) {
            Long count = counter.count(startDate, endDate, theme);
            counts.put(theme, count);
        }
    }

    public List<Theme> findTopPopular(final int size) {
        return counts.entrySet()
                .stream()
                .sorted(Entry.comparingByValue(Collections.reverseOrder()))
                .limit(size)
                .map(Entry::getKey)
                .toList();
    }
}
