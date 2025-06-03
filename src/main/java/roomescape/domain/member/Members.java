package roomescape.domain.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Members {

    private final List<Member> members;

    public Members(List<Member> members) {
        this.members = new ArrayList<>(members);
    }

    // 이메일로 회원 찾기
    public Optional<Member> findByEmail(Email email) {
        return members.stream()
                .filter(member -> member.hasSameEmail(email))
                .findFirst();
    }

    // 관리자만 필터링
    public Members filterAdmins() {
        List<Member> admins = members.stream()
                .filter(Member::canAccessAdminFeatures)
                .toList();
        return new Members(admins);
    }

    // 일반 사용자만 필터링
    public Members filterUsers() {
        List<Member> users = members.stream()
                .filter(member -> !member.canAccessAdminFeatures())
                .toList();
        return new Members(users);
    }

    // 특정 이름 패턴으로 검색
    public Members searchByNamePattern(String pattern) {
        List<Member> matching = members.stream()
                .filter(member -> member.getName().getValue().contains(pattern))
                .toList();
        return new Members(matching);
    }

    public List<Member> toList() {
        return new ArrayList<>(members);
    }

    public int size() {
        return members.size();
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

}
