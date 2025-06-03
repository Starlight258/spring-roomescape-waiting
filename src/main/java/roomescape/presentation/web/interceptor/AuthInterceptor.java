package roomescape.presentation.web.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.member.MemberService;
import roomescape.domain.member.Member;
import roomescape.presentation.security.JwtTokenProvider;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // OPTIONS 요청은 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 토큰 추출
        String token = extractToken(request);
        if (token == null) {
            sendUnauthorizedResponse(response, "토큰이 필요합니다.");
            return false;
        }

        // 토큰 검증
        if (!jwtTokenProvider.validateToken(token)) {
            sendUnauthorizedResponse(response, "유효하지 않은 토큰입니다.");
            return false;
        }

        // 사용자 정보 조회 및 컨텍스트에 저장
        String email = jwtTokenProvider.getEmailFromToken(token);
        Optional<Member> memberOpt = memberService.findMemberByEmail(email);

        if (memberOpt.isEmpty()) {
            sendUnauthorizedResponse(response, "존재하지 않는 사용자입니다.");
            return false;
        }

        // 요청 컨텍스트에 사용자 정보 저장
        request.setAttribute("currentMember", memberOpt.get());

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        // Cookie에서 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Header에서 토큰 추출 (Bearer 토큰)
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message)
            throws IOException, IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = String.format(
                "{\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message,
                LocalDateTime.now().toString()
        );

        response.getWriter().write(jsonResponse);
    }
}

