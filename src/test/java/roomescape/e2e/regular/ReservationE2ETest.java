package roomescape.e2e.regular;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.E2ETestFixture.DEFAULT_MEMBER_NAME;
import static roomescape.fixture.E2ETestFixture.DEFAULT_THEME_NAME;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.dto.response.reservation.MyReservationRetrievalResponse;
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
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
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
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
        E2ETestFixture.saveReservation(regularSessionId, UnitTestFixture.makeFutureDate(), timeId, themeId);
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void findMyReservations() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        Long timeId = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        Long timeId2 = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(11, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);

        String regularSessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
        LocalDate date = UnitTestFixture.makeFutureDate();
        E2ETestFixture.saveReservation(regularSessionId, date, timeId, themeId);
        E2ETestFixture.saveReservation(regularSessionId, date, timeId2, themeId);

        String regular2SessionId = E2ETestFixture.signUpRegularAndLogin("aina");
        Long timeId3 = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(12, 0));
        E2ETestFixture.saveWaiting(regular2SessionId, date, timeId, themeId);

        String regular3SessionId = E2ETestFixture.signUpRegularAndLogin("kali");
        E2ETestFixture.saveWaiting(regular3SessionId, date, timeId, themeId);
        E2ETestFixture.saveWaiting(regular3SessionId, date, timeId2, themeId);
        E2ETestFixture.saveReservation(regular3SessionId, date, timeId3, themeId);

        List<MyReservationRetrievalResponse> responses = RestAssured.given().log().all()
                .cookie("JSESSIONID", regular3SessionId)
                .when().get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responses.size()).isEqualTo(3);

            softAssertions.assertThat(responses.getFirst().time()).isEqualTo("10:00");
            softAssertions.assertThat(responses.getFirst().status()).isEqualTo("2번째 예약대기");

            softAssertions.assertThat(responses.get(1).time()).isEqualTo("11:00");
            softAssertions.assertThat(responses.get(1).status()).isEqualTo("1번째 예약대기");

            softAssertions.assertThat(responses.get(2).time()).isEqualTo("12:00");
            softAssertions.assertThat(responses.get(2).status()).isEqualTo("예약");
        });
    }

    @Test
    void deleteReservation() {
        saveReservation();
        String sessionId = E2ETestFixture.loginRegular(DEFAULT_MEMBER_NAME);

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

    @Test
    void promotedWaitingWhenDeleteReservation() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        Long timeId = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);

        String sessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
        LocalDate date = UnitTestFixture.makeFutureDate();
        E2ETestFixture.saveReservation(sessionId, date, timeId, themeId);
        String sessionId2 = E2ETestFixture.signUpRegularAndLogin("aina");
        E2ETestFixture.saveWaiting(sessionId2, date, timeId, themeId);

        RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        List<MyReservationRetrievalResponse> responses = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId2)
                .when().get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responses.size()).isOne();
            softAssertions.assertThat(responses.getFirst().date()).isEqualTo(date);
        });
    }
}
