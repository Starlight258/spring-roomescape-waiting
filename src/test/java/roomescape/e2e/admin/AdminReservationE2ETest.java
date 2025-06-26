package roomescape.e2e.admin;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.E2ETestFixture.DEFAULT_MEMBER_NAME;
import static roomescape.fixture.E2ETestFixture.DEFAULT_THEME_NAME;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.request.reservation.AdminReservationPreservationRequest;
import roomescape.fixture.E2ETestFixture;
import roomescape.fixture.UnitTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations=classpath:admin-data.sql"
})
public class AdminReservationE2ETest {

    private static final String FUTURE_DATE = UnitTestFixture.makeFutureDate().toString();

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void saveReservation() {
        Long memberId = E2ETestFixture.signUpRegular(DEFAULT_MEMBER_NAME);
        String sessionId = E2ETestFixture.loginAdmin();
        Long timeId = E2ETestFixture.saveReservationTime(sessionId, LocalTime.of(10, 0));
        Long themeId = E2ETestFixture.saveTheme(sessionId, DEFAULT_THEME_NAME);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(new AdminReservationPreservationRequest(memberId, FUTURE_DATE, timeId, themeId))
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
