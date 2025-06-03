package roomescape.presentation.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.security.AuthorizationExpressionEvaluator;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthorizationExpressionEvaluator expressionEvaluator;

    public AdminInterceptor(AuthorizationExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Member currentMember = (Member) request.getAttribute("currentMember");

        // 메서드 레벨 @PreAuthorize 확인
        PreAuthorize methodPreAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
        if (methodPreAuthorize != null) {
            return checkAuthorization(methodPreAuthorize.value(), currentMember, response);
        }

        // 클래스 레벨 @PreAuthorize 확인
        PreAuthorize classPreAuthorize = handlerMethod.getBeanType().getAnnotation(PreAuthorize.class);
        if (classPreAuthorize != null) {
            return checkAuthorization(classPreAuthorize.value(), currentMember, response);
        }

        return true;
    }

    private boolean checkAuthorization(String expression, Member member, HttpServletResponse response)
            throws IOException {
        boolean hasPermission = expressionEvaluator.evaluate(expression, member);

        if (!hasPermission) {
            if (member == null) {
                sendUnauthorizedResponse(response, "로그인이 필요합니다.");
            } else {
                sendForbiddenResponse(response, "접근 권한이 없습니다.");
            }
            return false;
        }

        return true;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = String.format(
                "{\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message,
                LocalDateTime.now().toString()
        );

        response.getWriter().write(jsonResponse);
    }

    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = String.format(
                "{\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message,
                LocalDateTime.now().toString()
        );

        response.getWriter().write(jsonResponse);
    }
}
