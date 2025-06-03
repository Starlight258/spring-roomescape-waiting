package roomescape.domain.member;

public interface EmailDuplicationChecker {

    boolean isDuplicated(Email email);
}
