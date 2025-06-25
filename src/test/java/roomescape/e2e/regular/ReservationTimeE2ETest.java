package roomescape.e2e.regular;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.E2ETestFixture.DEFAULT_MEMBER_NAME;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.response.reservationtime.ReservationTimeAvailableResponse;
import roomescape.fixture.E2ETestFixture;
import roomescape.fixture.UnitTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations=classpath:admin-data.sql"
})
public class ReservationTimeE2ETest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void saveReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");
        String adminSessionId = E2ETestFixture.loginAdmin();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", adminSessionId)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void findAllAvailable() {
        // given
        String adminSessionId = E2ETestFixture.loginAdmin();
        Long timeId1 = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(11, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, E2ETestFixture.DEFAULT_THEME_NAME);
        LocalDate date = UnitTestFixture.makeFutureDate();
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
        E2ETestFixture.saveReservation(regularSessionId, date, timeId1, themeId);

        // when
        List<ReservationTimeAvailableResponse> responses = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .params("date", date.toString(), "themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responses.getFirst().alreadyBooked()).isTrue();
            softAssertions.assertThat(responses.get(1).alreadyBooked()).isFalse();
        });
    }

    @Test
    void deleteReservationTime() {
        saveReservationTime();
        String adminSessionId = E2ETestFixture.loginAdmin();

        RestAssured.given().log().all()
                .cookie("JSESSIONID", adminSessionId)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

}
