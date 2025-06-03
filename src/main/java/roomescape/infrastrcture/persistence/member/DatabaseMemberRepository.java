package roomescape.infrastrcture.persistence.member;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Repository
public class DatabaseMemberRepository implements MemberRepository {

    private final MemberJpaRepository jpaRepository;

    public DatabaseMemberRepository(MemberJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Member save(Member member) {
        MemberEntity entity = MemberEntity.from(member);
        MemberEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jpaRepository.findById(id)
                .map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(MemberEntity::toDomain);
    }

    @Override
    public List<Member> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(MemberEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public void delete(Member member) {
        MemberEntity entity = MemberEntity.from(member);
        jpaRepository.delete(entity);
    }
}
