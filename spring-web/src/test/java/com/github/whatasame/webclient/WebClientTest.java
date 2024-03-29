package com.github.whatasame.webclient;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import javax.naming.AuthenticationException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class WebClientTest {

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
    @DisplayName("200 반환 시 정상적으로 응답을 객체로 변환한다.")
    void status200() {
        /* given */
        mockWebServer.enqueue(
                new MockResponse()
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(
                                """
                                    {
                                      "email": "test@email.com",
                                      "password": "password"
                                    }
                                """)
                        .setResponseCode(200));

        /* when */
        final Mono<Member> memberMono =
                webClient.get().uri("/member/me").retrieve().bodyToMono(Member.class);

        /* then */
        StepVerifier.create(memberMono)
                .expectNext(new Member("test@email.com", "password"))
                .verifyComplete();
    }

    @Test
    @DisplayName("4xx 반환 시 예외를 던진다.")
    void status4xx() {
        /* given */
        mockWebServer.enqueue(
                new MockResponse()
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(
                                """
                                  "error": "Not Found"
                                """)
                        .setResponseCode(404));

        /* when */
        final Mono<Member> memberMono =
                webClient
                        .get()
                        .uri("/member/me")
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::is4xxClientError,
                                response ->
                                        Mono.error(
                                                new IllegalArgumentException("Invalid request.")))
                        .bodyToMono(Member.class);

        /* then */
        StepVerifier.create(memberMono)
                .expectErrorMatches(
                        throwable ->
                                throwable instanceof IllegalArgumentException
                                        && throwable.getMessage().equals("Invalid request."))
                .verify();
    }

    @Test
    @DisplayName("401 반환 시 구체적인 예외를 던진다.")
    void status401() {
        /* given */
        mockWebServer.enqueue(
                new MockResponse()
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(
                                """
                                  "error": "Unauthorized"
                                """)
                        .setResponseCode(401));

        /* when */
        final Mono<Member> memberMono =
                webClient
                        .get()
                        .uri("/member/me")
                        .retrieve()
                        .onStatus(
                                status ->
                                        status.isSameCodeAs(
                                                HttpStatus.UNAUTHORIZED), // order is important
                                response ->
                                        Mono.error(
                                                new AuthenticationException(
                                                        "Not allowed to access.")))
                        .onStatus(
                                HttpStatusCode::is4xxClientError,
                                response ->
                                        Mono.error(
                                                new IllegalArgumentException("Invalid request.")))
                        .bodyToMono(Member.class);

        /* then */
        StepVerifier.create(memberMono)
                .expectErrorMatches(
                        throwable ->
                                throwable instanceof AuthenticationException
                                        && throwable.getMessage().equals("Not allowed to access."))
                .verify();
    }

    @Test
    @DisplayName("Timeout 시 예외를 던진다.")
    void timeout() {
        /* given */
        mockWebServer.enqueue(
                new MockResponse()
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(
                                """
                                {
                                  "email": "test@email.com",
                                  "password": "password"
                                  }""")
                        .setBodyDelay(500, MILLISECONDS));

        /* when */
        final Mono<Member> memberMono =
                webClient
                        .get()
                        .uri("/member/me")
                        .retrieve()
                        .bodyToMono(Member.class)
                        .timeout(Duration.ofMillis(100));

        /* then */
        StepVerifier.create(memberMono).expectError(TimeoutException.class).verify();
    }

    record Member(String email, String password) {}
}
