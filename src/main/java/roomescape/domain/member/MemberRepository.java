package roomescape.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(Email email);

    List<Member> findAll();

    boolean existsByEmail(Email email);

    void delete(Member member);
}
