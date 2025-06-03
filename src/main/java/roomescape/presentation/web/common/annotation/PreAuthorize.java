package roomescape.presentation.web.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {

    /**
     * 권한 표현식
     * 예: "hasRole('ADMIN')", "hasRole('USER')", "isAuthenticated()"
     */
    String value();
}
