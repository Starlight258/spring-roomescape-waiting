package roomescape.presentation.web.member.dto.response;

import roomescape.domain.member.Member;

public class LoginResponse {

    private final String name;
    private final String role;
    private final String token;

    public LoginResponse(String name, String role, String token) {
        this.name = name;
        this.role = role;
        this.token = token;
    }

    public static LoginResponse from(Member member, String token) {
        return new LoginResponse(
                member.getName().getValue(),
                member.getRole().name(),
                token
        );
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}

