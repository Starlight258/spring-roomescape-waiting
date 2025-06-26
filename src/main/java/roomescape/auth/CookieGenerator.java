package roomescape.auth;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieGenerator {

    public ResponseCookie makeCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .path("/")
                .maxAge(15 * 60)
                .build();
    }

}
