package roomescape.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.exception.UnAuthorizedException;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        boolean isMemberPrinciple = clazz.equals(MemberPrinciple.class);
        Class<?> controllerClass = parameter.getContainingClass();
        boolean hasRequireRoleOnClass = controllerClass.isAnnotationPresent(RequireRole.class);
        boolean hasRequireRoleOnMethod = parameter.getMethodAnnotation(RequireRole.class) != null;
        return isMemberPrinciple && (hasRequireRoleOnClass || hasRequireRoleOnMethod);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        Long memberId = (Long) webRequest.getAttribute("token", RequestAttributes.SCOPE_SESSION);
        if (memberId == null) {
            throw new UnAuthorizedException("Session is invalidate");
        }
        return new MemberPrinciple(memberId);
    }
}
