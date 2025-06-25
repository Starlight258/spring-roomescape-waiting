package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.MemberRole;
import roomescape.exception.ForbiddenException;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (role == null || MemberRole.valueOf(role) != MemberRole.ADMIN) {
            throw new ForbiddenException("Forbidden request");
        }
        return true;
    }
}
