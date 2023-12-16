package com.github.whatasame.thymeleaf;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThymeleafTest {

    @LocalServerPort int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @DisplayName("Variable expression을 사용하여 템플릿을 렌더링한다.")
    void variableExpression() {
        given().when()
                .get("/thymeleaf/meals")
                .then()
                .statusCode(200)
                .body(containsString("<p>Breakfast: <span>hamburger</span></p>"))
                .body(containsString("<p>Launch: <span>pizza</span></p>"))
                .body(containsString("<p>Dinner: <span>chicken</span></p>"));
    }
}
