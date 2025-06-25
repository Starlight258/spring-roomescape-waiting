package roomescape.e2e.regular;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.E2ETestFixture.DEFAULT_THEME_NAME;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.theme.ThemePreservationRequest;
import roomescape.dto.response.theme.ThemePopularResponse;
import roomescape.fixture.E2ETestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations=classpath:admin-data.sql"
})
public class ThemeE2ETest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void saveTheme() {
        String adminSessionId = E2ETestFixture.loginAdmin();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", adminSessionId)
                .body(new ThemePreservationRequest("기억저장소", "memory", "thumbnail.png"))
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Sql("/themes/top-popular.sql")
    void findTopPopular() {
        List<ThemePopularResponse> responses = RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responses.getFirst().name()).isEqualTo("감성");
            softAssertions.assertThat(responses.get(1).name()).isEqualTo("모험");
            softAssertions.assertThat(responses.size()).isEqualTo(10);
        });
    }

    @Test
    void findThemes() {
        String adminSessionId = E2ETestFixture.loginAdmin();
        E2ETestFixture.saveTheme(adminSessionId, DEFAULT_THEME_NAME);
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void deleteTheme() {
        saveTheme();
        String adminSessionId = E2ETestFixture.loginAdmin();

        RestAssured.given().log().all()
                .cookie("JSESSIONID", adminSessionId)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
