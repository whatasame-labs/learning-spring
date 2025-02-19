package com.whatasame.session;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SessionTest {

    @LocalServerPort
    int port;

    SessionFilter sessionFilter;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        sessionFilter = new SessionFilter();
    }

    @Test
    @DisplayName("서버에 처음 접속하면 새로운 세션이 생성된다.")
    void newSession() {
        RestAssured.given()
                .when()
                .get("/session")
                .then()
                .statusCode(200)
                .body(equalTo("Greeting new user! You are the first time visitor."));
    }

    @Test
    @DisplayName("서버에 두 번째 접속하면 기존 세션이 유지된다.")
    void oldSession() {
        RestAssured.given().filter(sessionFilter).when().get("/session").then().statusCode(200);

        RestAssured.given()
                .filter(sessionFilter)
                .when()
                .get("/session")
                .then()
                .statusCode(200)
                .body(equalTo("Hello again, you have already visited this server 1 times."));
    }
}
