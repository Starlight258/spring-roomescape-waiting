package roomescape.domain.member;

import java.util.Optional;

public interface MemberFinder {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(Email email);

    Members findAll();
}
