# WebClient

## How to Handle WebClient response

### When response is OK

If `WebClient` response is OK, we will just get the response body happily.

```java
void ok(){
    Mono<String> monoString = webClient.get()
            .retrieve()
            .bodyToMono(String.class);
            
    monoString.subscribe(System.out::println);
} 
```

### When response is not OK

If `WebClient` response is not OK, we can handle it using `.onStatus` method.

```java
void notOk(){
    Mono<String> monoString = webClient.get()
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, response -> {
                return Mono.error(new RuntimeException("4xx"));
            })
            .onStatus(HttpStatus::is5xxServerError, response -> {
                return Mono.error(new RuntimeException("5xx"));
            })
            .bodyToMono(String.class);
            
    monoString.subscribe(System.out::println, System.err::println);
}
```

### Handle specific status code

In order to handle specific status code, we can use `.onStatus` method with `HttpStatus` enum.

```java
void specificStatus(){
    Mono<String> monoString = webClient.get()
            .retrieve()
            .onStatus(status -> status.isSameCodeAs(HttpStatus.BAD_REQUEST), response -> {
                return Mono.error(new RuntimeException("404"));
            })
            .onStatus(HttpStatus::is4xxClientError, response -> {
                return Mono.error(new RuntimeException("4xx"));
            })
            .onStatus(HttpStatus::is5xxServerError, response -> {
                return Mono.error(new RuntimeException("5xx"));
            })
            .bodyToMono(String.class);
            
    monoString.subscribe(System.out::println, System.err::println);
}

Note that it is important to align the order of the `onStatus` method. If you put `onStatus` method with `HttpStatus.BAD_REQUEST` at the end, it will not work as expected.
```

## How to mock server test with `WebClient`

There is two ways to mock server test with `WebClient`:

* Mokito
* MockWebServer (from OkHttp)

### Mokito

At first, we make mock instance using `MocitoExtension` and `@Mock` annotation.

```java
@ExtendWith(MockitoExtension.class)
public class MokitoWebServerTest {

    @Mock WebClient webClientMock;

    @Mock WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @Mock WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock WebClient.RequestBodySpec requestBodySpecMock;

    @Mock WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock WebClient.ResponseSpec responseSpecMock;
}
```

Then, we can use `when` method to mock the behavior of the method.

```java
@Test
void test() {
    /* given */
    when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
    when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
    when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("Hello, World!"));
}
```

Now our `WebClient` is mocked. You can test WebClient and verify the result using `StepVerifier`.

```java
@Test
void test(){
    /* given */
    // ...
    
    /* when */
    Mono<String> response = webClientMock.get()
            .uri("http://localhost:8080")
            .retrieve()
            .bodyToMono(String.class);
            
    /* then */
    StepVerifier.create(response)
            .expectNext("Hello, World!")
            .verifyComplete();
}

```

But this way have many boilerplate code. So, we can use `MockWebServer` to simplify the code.

### MockWebServer

`MockWebServer` is a part of OkHttp. It is a scriptable web server for testing HTTP clients. It is useful for testing
your HTTP client against a mock server.

Fortunately, OkHttp provides separate module for `MockWebServer` so we don't have to include unnecessary dependencies.

```gradle
dependencies {
    testImplementation 'com.squareup.okhttp3:mockwebserver:4.12.0' // latest version
}
```

With `MockWebServer`, what we need to do is just create mock server instance and start it.

```java
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
}
```

Unlike `Mockito`, we don't need to mock the behavior of the method. We just need to enqueue the response.

```java
@Test
void test() {
    /* given */
    mockWebServer.enqueue(
            new MockResponse()
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .setBody("Hello, World!"));

    /* when */
    Mono<String> response = webClient.get()
            .retrieve()
            .bodyToMono(String.class);

    /* then */
    StepVerifier.create(response)
            .expectNext("Hello, World!")
            .verifyComplete();
}
```

`MockResponse` support many features like throttling, delay, and so on.

```java
@Test
void test() {
    /* given */
    mockWebServer.enqueue(
            new MockResponse()
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .setBody("Hello, World!")
                    .setBodydelay(1, TimeUnit.SECONDS));
                    .throttleBody(1, 1, TimeUnit.SECONDS));
                    
    // ...
}
```

## References

* [Mocking a WebClient in Spring - Baeldung](https://www.baeldung.com/spring-mocking-webclient)
* [MockWebServer - OkHttp](https://github.com/square/okhttp/blob/master/mockwebserver/README.md)


