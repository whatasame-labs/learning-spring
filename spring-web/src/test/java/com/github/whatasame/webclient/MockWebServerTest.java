package com.github.whatasame.webclient;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * reference: <a href="https://github.com/square/okhttp/blob/master/mockwebserver/README.md">OkHttp MockWebServer
 * docs</a>
 */
public class MockWebServerTest {

    static MockWebServer mockWebServer;
    static int port;
    static WebClient webClient;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        port = mockWebServer.getPort();

        webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("GET 요청 테스트: 회원 정보 조회")
    void testGet() {
        /* given */
        mockWebServer.enqueue(new MockResponse()
                .setBody(
                        """
                        {
                          "email": "test@email.com",
                          "password": "password"
                        }
                        """)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        /* when */
        final Mono<Member> memberMono =
                webClient.get().uri("/member/me").retrieve().bodyToMono(Member.class);

        /* then */
        StepVerifier.create(memberMono)
                .expectNext(new Member("test@email.com", "password"))
                .verifyComplete();
    }

    @Test
    @DisplayName("POST 요청 테스트: 회원 가입")
    void testPost() {
        /* given */
        mockWebServer.enqueue(new MockResponse()
                .setBody("777")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        /* when */
        final Mono<Long> memberIdMono = webClient
                .post()
                .uri("/member/signup")
                .bodyValue(new Member("test@email.com", "password")) // use same member object as mock parameter
                .retrieve()
                .bodyToMono(Long.class);

        /* then */
        StepVerifier.create(memberIdMono).expectNext(777L).verifyComplete();
    }

    record Member(String email, String password) {}
}
