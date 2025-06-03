package roomescape.domain.common.exceptions;

public class DuplicatedException extends DomainException {

    public DuplicatedException(final String message) {
        super(message);
    }
}
