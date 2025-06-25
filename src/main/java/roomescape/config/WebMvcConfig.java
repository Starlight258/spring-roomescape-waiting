package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminHandlerInterceptor;
import roomescape.auth.AuthArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;
    private final AdminHandlerInterceptor adminHandlerInterceptor;

    public WebMvcConfig(final AuthArgumentResolver authArgumentResolver,
                        final AdminHandlerInterceptor adminHandlerInterceptor) {
        this.authArgumentResolver = authArgumentResolver;
        this.adminHandlerInterceptor = adminHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminHandlerInterceptor)
                .addPathPatterns("/admin/**");
    }
}
