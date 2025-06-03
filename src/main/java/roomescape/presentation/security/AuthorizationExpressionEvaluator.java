package roomescape.presentation.security;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

@Component
public class AuthorizationExpressionEvaluator {

    /**
     * 권한 표현식을 평가합니다.
     * @param expression 권한 표현식 (예: "hasRole('ADMIN')")
     * @param member 현재 사용자
     * @return 권한이 있으면 true, 없으면 false
     */
    public boolean evaluate(String expression, Member member) {
        if (member == null) {
            return evaluateAnonymousExpression(expression);
        }

        return evaluateAuthenticatedExpression(expression, member);
    }

    private boolean evaluateAnonymousExpression(String expression) {
        // 익명 사용자가 접근 가능한 표현식들
        switch (expression.toLowerCase()) {
            case "permitall()":
            case "permitall":
                return true;
            case "isauthenticated()":
            case "isauthenticated":
                return false;
            default:
                if (expression.toLowerCase().startsWith("hasrole(")) {
                    return false; // 익명 사용자는 어떤 역할도 없음
                }
                return false;
        }
    }

    private boolean evaluateAuthenticatedExpression(String expression, Member member) {
        String lowerExpression = expression.toLowerCase().trim();

        // 인증된 사용자 체크
        if (lowerExpression.equals("isauthenticated()") || lowerExpression.equals("isauthenticated")) {
            return true;
        }

        // 모든 접근 허용
        if (lowerExpression.equals("permitall()") || lowerExpression.equals("permitall")) {
            return true;
        }

        // 역할 기반 권한 체크
        if (lowerExpression.startsWith("hasrole(")) {
            return evaluateRoleExpression(lowerExpression, member);
        }

        // 사용자 ID 기반 체크
        if (lowerExpression.startsWith("hasid(")) {
            return evaluateIdExpression(lowerExpression, member);
        }

        // 복합 표현식 (AND, OR)
        if (lowerExpression.contains(" and ") || lowerExpression.contains(" or ")) {
            return evaluateComplexExpression(lowerExpression, member);
        }

        // 기본적으로 접근 거부
        return false;
    }

    private boolean evaluateRoleExpression(String expression, Member member) {
        // hasRole('ADMIN'), hasRole('USER') 등을 파싱
        String rolePattern = "hasrole\\(['\"]([^'\"]+)['\"]\\)";
        Pattern pattern = Pattern.compile(rolePattern);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            String requiredRole = matcher.group(1).toUpperCase();

            switch (requiredRole) {
                case "ADMIN":
                    return member.getRole() == MemberRole.ADMIN;
                case "USER":
                    return member.getRole() == MemberRole.USER;
                default:
                    return false;
            }
        }

        return false;
    }

    private boolean evaluateIdExpression(String expression, Member member) {
        // hasId('123') 형태의 표현식 처리
        String idPattern = "hasid\\(['\"]([^'\"]+)['\"]\\)";
        Pattern pattern = Pattern.compile(idPattern);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            try {
                Long requiredId = Long.parseLong(matcher.group(1));
                return member.getId().equals(requiredId);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    private boolean evaluateComplexExpression(String expression, Member member) {
        // 간단한 AND/OR 처리
        if (expression.contains(" and ")) {
            String[] parts = expression.split(" and ");
            return Arrays.stream(parts)
                    .allMatch(part -> evaluate(part.trim(), member));
        }

        if (expression.contains(" or ")) {
            String[] parts = expression.split(" or ");
            return Arrays.stream(parts)
                    .anyMatch(part -> evaluate(part.trim(), member));
        }

        return false;
    }
}

