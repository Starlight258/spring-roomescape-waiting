package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.MemberRole;
import roomescape.exception.ForbiddenException;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            RequireRole classAnnotation = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
            if (classAnnotation != null) {
                validateSession(request, classAnnotation.role());
            }
            RequireRole methodAnnotation = handlerMethod.getMethodAnnotation(RequireRole.class);
            if (methodAnnotation != null) {
                validateSession(request, methodAnnotation.role());
            }
        }
        return true;
    }

    private void validateSession(final HttpServletRequest request, final MemberRole expectedRole) {
        HttpSession session = request.getSession();
        String actualRole = (String) session.getAttribute("role");
        if (actualRole == null || !MemberRole.valueOf(actualRole).canAccess(expectedRole)) {
            throw new ForbiddenException("Forbidden request");
        }
    }
}
