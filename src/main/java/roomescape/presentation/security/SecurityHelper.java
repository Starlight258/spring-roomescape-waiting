package roomescape.presentation.security;

import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

@Component
public class SecurityHelper {

    private final AuthorizationExpressionEvaluator expressionEvaluator;

    public SecurityHelper(AuthorizationExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    /**
     * 현재 사용자가 관리자인지 확인
     */
    public static boolean isAdmin(Member member) {
        return member != null && member.canAccessAdminFeatures();
    }

    /**
     * 현재 사용자가 특정 역할을 가지고 있는지 확인
     */
    public static boolean hasRole(Member member, MemberRole role) {
        return member != null && member.getRole() == role;
    }

    /**
     * 현재 사용자가 특정 사용자와 같은지 확인 (본인 확인)
     */
    public static boolean isSameUser(Member currentMember, Member targetMember) {
        return currentMember != null && targetMember != null &&
                currentMember.isSameMember(targetMember);
    }

    /**
     * 현재 사용자가 리소스에 접근할 권한이 있는지 확인 (소유자 또는 관리자)
     */
    public static boolean canAccessResource(Member currentMember, Member resourceOwner) {
        return isAdmin(currentMember) || isSameUser(currentMember, resourceOwner);
    }

    /**
     * 표현식을 동적으로 평가
     */
    public boolean checkPermission(String expression, Member member) {
        return expressionEvaluator.evaluate(expression, member);
    }
}
