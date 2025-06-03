package roomescape.infrastrcture.persistence.member;

import org.springframework.stereotype.Component;
import roomescape.domain.member.Email;
import roomescape.domain.member.EmailDuplicationChecker;

@Component
public class DatabaseEmailDuplicationChecker implements EmailDuplicationChecker {

    private final MemberJpaRepository memberJpaRepository;

    public DatabaseEmailDuplicationChecker(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public boolean isDuplicated(Email email) {
        return memberJpaRepository.existsByEmail(email.getValue());
    }
}

