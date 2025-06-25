package roomescape.e2e.regular;

import static org.hamcrest.Matchers.is;
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
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.fixture.E2ETestFixture;
import roomescape.fixture.UnitTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations=classpath:admin-data.sql"
})
public class ReservationE2ETest {

    private static final String FUTURE_DATE = UnitTestFixture.makeFutureDate().toString();

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void saveReservation() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin();
        Long timeId = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", regularSessionId)
                .body(new RegularReservationPreservationRequest(FUTURE_DATE, timeId, themeId))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void findReservations() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        Long timeId = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin();
        E2ETestFixture.saveReservation(regularSessionId, UnitTestFixture.makeFutureDate(), timeId, themeId);
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void deleteReservation() {
        saveReservation();
        String sessionId = E2ETestFixture.loginRegular();

        RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
