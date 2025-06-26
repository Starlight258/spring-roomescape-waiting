package roomescape.e2e.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.E2ETestFixture.DEFAULT_MEMBER_NAME;
import static roomescape.fixture.E2ETestFixture.DEFAULT_THEME_NAME;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.response.waiting.WaitingRetrievalResponse;
import roomescape.fixture.E2ETestFixture;
import roomescape.fixture.UnitTestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations=classpath:admin-data.sql"
})
public class AdminWaitingE2ETest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findAll() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        Long timeId = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
        LocalDate date = UnitTestFixture.makeFutureDate();
        E2ETestFixture.saveReservation(regularSessionId, date, timeId, themeId);

        String regularSessionId2 = E2ETestFixture.signUpRegularAndLogin("aina");
        E2ETestFixture.saveWaiting(regularSessionId2, date, timeId, themeId);

        List<WaitingRetrievalResponse> responses = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", adminSessionId)
                .when().get("/admin/waitings")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });
        assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void deleteWaiting() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        Long timeId = E2ETestFixture.saveReservationTime(adminSessionId, LocalTime.of(10, 0));
        Long themeId = E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);
        String regularSessionId = E2ETestFixture.signUpRegularAndLogin(DEFAULT_MEMBER_NAME);
        LocalDate date = UnitTestFixture.makeFutureDate();
        E2ETestFixture.saveReservation(regularSessionId, date, timeId, themeId);

        String regular2SessionId = E2ETestFixture.signUpRegularAndLogin("aina");
        E2ETestFixture.saveWaiting(regular2SessionId, date, timeId, themeId);

        RestAssured.given().log().all()
                .cookie("JSESSIONID", regular2SessionId)
                .when().delete("/waiting/1")
                .then().log().all()
                .statusCode(204);
    }
}
