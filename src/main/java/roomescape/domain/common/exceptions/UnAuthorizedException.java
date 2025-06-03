package roomescape.domain.common.exceptions;

public class UnAuthorizedException extends DomainException {

    public UnAuthorizedException(final String message) {
        super(message);
    }
}
