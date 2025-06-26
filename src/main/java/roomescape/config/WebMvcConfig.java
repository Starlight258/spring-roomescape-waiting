package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthArgumentResolver;
import roomescape.auth.AuthHandlerInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;
    private final AuthHandlerInterceptor authHandlerInterceptor;

    public WebMvcConfig(final AuthArgumentResolver authArgumentResolver,
                        final AuthHandlerInterceptor authHandlerInterceptor) {
        this.authArgumentResolver = authArgumentResolver;
        this.authHandlerInterceptor = authHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor);
    }
}
