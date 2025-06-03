package roomescape.infrastrcture.persistence.member;

import java.util.HashSet;
import java.util.Set;
import roomescape.domain.member.Email;
import roomescape.domain.member.EmailDuplicationChecker;

public class InMemoryEmailDuplicationChecker implements EmailDuplicationChecker {

    private final Set<String> existingEmails = new HashSet<>();

    @Override
    public boolean isDuplicated(Email email) {
        return existingEmails.contains(email.getValue());
    }

    public void addExistingEmail(String email) {
        existingEmails.add(email);
    }

    public void clear() {
        existingEmails.clear();
    }
}
