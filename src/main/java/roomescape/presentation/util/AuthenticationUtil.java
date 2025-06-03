package roomescape.presentation.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.domain.common.exceptions.UnAuthorizedException;
import roomescape.domain.member.Member;

@Component
public class AuthenticationUtil {

    public static Member getCurrentMember(HttpServletRequest request) {
        Member currentMember = (Member) request.getAttribute("currentMember");
        if (currentMember == null) {
            throw new UnAuthorizedException("인증된 사용자 정보를 찾을 수 없습니다.");
        }
        return currentMember;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        try {
            Member currentMember = getCurrentMember(request);
            return currentMember.canAccessAdminFeatures();
        } catch (UnAuthorizedException e) {
            return false;
        }
    }

    public static boolean isOwner(HttpServletRequest request, Member resourceOwner) {
        try {
            Member currentMember = getCurrentMember(request);
            return currentMember.isSameMember(resourceOwner);
        } catch (UnAuthorizedException e) {
            return false;
        }
    }
}
