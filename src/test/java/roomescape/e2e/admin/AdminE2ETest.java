package roomescape.e2e.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.fixture.E2ETestFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.sql.init.data-locations=classpath:admin-data.sql"
})
public class AdminE2ETest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void showAdminHomePage() {
        String SESSIONID = E2ETestFixture.loginAdmin();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", SESSIONID)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void showAdminReservationPage() {
        String SESSIONID = E2ETestFixture.loginAdmin();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", SESSIONID)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }
}
