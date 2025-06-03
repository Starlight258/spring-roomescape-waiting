package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.presentation.web.interceptor.AdminInterceptor;
import roomescape.presentation.web.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(AuthInterceptor authInterceptor, AdminInterceptor adminInterceptor) {
        this.authInterceptor = authInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 인증 인터셉터 (토큰 검증)
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/members/login", "/members/signup", "/themes", "/times");

        // 2. 권한 인터셉터 (@PreAuthorize 처리)
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**");  // 모든 경로에서 @PreAuthorize 체크
    }
}


