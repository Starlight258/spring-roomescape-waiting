package roomescape.domain.reservation;

public enum ReservationStatus {

    RESERVED("예약");

    private final String viewName;

    ReservationStatus(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
