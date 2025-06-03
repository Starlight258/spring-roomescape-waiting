package roomescape.infrastrcture.security;

import roomescape.domain.member.PasswordEncoder;

public class MockPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return "encoded_" + rawPassword;  // 단순한 prefix 추가
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encodedPassword.equals("encoded_" + rawPassword);
    }
}

