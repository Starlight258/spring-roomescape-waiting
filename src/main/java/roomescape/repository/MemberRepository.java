package roomescape.repository;

import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByName(MemberName name);

    boolean existsByEmail(@NotBlank String email);

    boolean existsByEmailAndPassword(@NotBlank String email, @NotBlank String password);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
