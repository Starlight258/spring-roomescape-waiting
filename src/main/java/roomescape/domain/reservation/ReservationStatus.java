package roomescape.domain.reservation;

public enum ReservationStatus {

    RESERVED("예약"), WAITING("%d번째 예약대기");

    private final String viewName;

    ReservationStatus(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
