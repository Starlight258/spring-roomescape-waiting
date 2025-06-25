package roomescape.fixture;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.dto.request.member.LoginRequest;
import roomescape.dto.request.member.SignupRequest;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.dto.request.theme.ThemePreservationRequest;
import roomescape.dto.request.waiting.WaitingPreservationRequest;
import roomescape.dto.response.member.SignupResponse;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.reservationtime.ReservationTimePreservationResponse;
import roomescape.dto.response.theme.ThemeRetrievalResponse;
import roomescape.dto.response.waiting.WaitingPreservationResponse;

public class E2ETestFixture {

    public static final String DEFAULT_MEMBER_NAME = "mint";
    public static final String DEFAULT_THEME_NAME = "기억저장소";

    public static Long saveReservationTime(String sessionId, LocalTime time) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", time.toString());

        ReservationTimePreservationResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(new TypeRef<>() {
                });

        return response.id();
    }

    public static Long saveTheme(String sessionId, String name) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(new ThemePreservationRequest(name, "memory", "thumbnail.png"))
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        List<ThemeRetrievalResponse> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .extract()
                .as(new TypeRef<>() {
                });

        return themes.getFirst().id();
    }

    public static Long saveReservation(String sessionId, LocalDate date, Long timeId, Long themeId) {
        ReservationPreservationResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(new RegularReservationPreservationRequest(date.toString(), timeId, themeId))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(ReservationPreservationResponse.class);

        return response.id();
    }

    public static Long saveWaiting(String sessionId, LocalDate date, Long timeId, Long themeId) {
        WaitingPreservationResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(new WaitingPreservationRequest(date.toString(), timeId, themeId))
                .when().post("/waiting")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(WaitingPreservationResponse.class);
        return response.id();
    }

    public static Long signUpRegular(String name) {
        SignupResponse signupResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new SignupRequest(name, name + "@gmail.com", "password"))
                .when().post("/members")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(SignupResponse.class);
        return signupResponse.id();
    }

    public static String loginRegular(final String name) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(name + "@gmail.com", "password"))
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");
    }

    public static String signUpRegularAndLogin(String name) {
        signUpRegular(name);
        return loginRegular(name);
    }

    public static String loginAdmin() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin@gmail.com", "password"))
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");
    }
}
