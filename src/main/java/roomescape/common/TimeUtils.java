package roomescape.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.exception.BadRequestException;

public class TimeUtils {

    public static LocalDate parseLocalDate(final String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("유효하지 않은 날짜입니다.");
        }
    }

    public static LocalTime parseLocalTime(final String time) {
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("유효하지 않은 시간입니다.");
        }
    }
}
