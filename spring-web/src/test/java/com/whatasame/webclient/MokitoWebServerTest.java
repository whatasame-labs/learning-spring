package com.whatasame.webclient;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class MokitoWebServerTest {

    @Mock
    WebClient webClientMock;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    WebClient.RequestBodySpec requestBodySpecMock;

    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock
    WebClient.ResponseSpec responseSpecMock;

    @Test
    @DisplayName("GET 요청 테스트: 회원 정보 조회")
    void testGet() {
        /* given */
        final Member member = new Member("test@email.com", "password");
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("/member/me")).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Member.class)).thenReturn(Mono.just(member));

        /* when */
        final Mono<Member> memberMono =
                webClientMock.get().uri("/member/me").retrieve().bodyToMono(Member.class);

        /* then */
        StepVerifier.create(memberMono)
                .expectNext(new Member("test@email.com", "password"))
                .verifyComplete();
    }

    @Test
    @DisplayName("POST 요청 테스트: 회원 가입")
    void testPost() {
        /* given */
        final Member member = new Member("test@email.com", "password");
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("/member/signup")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(member)).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Long.class)).thenReturn(Mono.just(777L));

        /* when */
        final Mono<Long> memberIdMono = webClientMock
                .post()
                .uri("/member/signup")
                .bodyValue(member) // use same member object as mock parameter
                .retrieve()
                .bodyToMono(Long.class);

        /* then */
        StepVerifier.create(memberIdMono).expectNext(777L).verifyComplete();
    }

    record Member(String email, String password) {}
}
