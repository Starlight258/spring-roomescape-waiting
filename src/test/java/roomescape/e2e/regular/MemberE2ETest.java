package roomescape.e2e.regular;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.fixture.E2ETestFixture.DEFAULT_MEMBER_NAME;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.request.member.LoginRequest;
import roomescape.dto.request.member.SignupRequest;
import roomescape.dto.response.member.CheckLoginResponse;
import roomescape.fixture.E2ETestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations="
})
public class MemberE2ETest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void signup() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignupRequest("mint", "mint@gmail.com", "password"))
                .when().post("/members")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    void login() {
        signup();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("mint@gmail.com", "password"))
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("JSESSIONID", notNullValue());
    }

    @Test
    void checkLogin() {
        String sessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);

        CheckLoginResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(CheckLoginResponse.class);

        assertThat(response.name()).isEqualTo(DEFAULT_MEMBER_NAME);
    }

}
