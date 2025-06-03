package roomescape.infrastrcture.security;

import org.springframework.stereotype.Component;
import roomescape.domain.member.PasswordEncoder;

@Component
public class BCryptPasswordEncoder implements PasswordEncoder {

    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder bcrypt = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return bcrypt.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return bcrypt.matches(rawPassword, encodedPassword);
    }
}

