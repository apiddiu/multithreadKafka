package com.aldo.kafka.multithreadKafka;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class EndpointMockTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8090);

    @Test
    public void callEndpointTest() throws InterruptedException {
        givenEndpointReturns();
        WebClient client = WebClient.create("http://localhost:8090");
        client.get().uri("/pippo").
                retrieve()
                .bodyToMono(String.class)
                .map(s ->
                        new StringBuilder()
                                .append("---------------------------------------").append(System.lineSeparator())
                                .append("---------------------------------------").append(System.lineSeparator())
                                .append("---------------------------------------").append(System.lineSeparator())
                                .append(s).append(System.lineSeparator())
                                .append("---------------------------------------").append(System.lineSeparator())
                                .append("---------------------------------------").append(System.lineSeparator())
                                .append("---------------------------------------").append(System.lineSeparator())
                                .toString()
                )
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    private void givenEndpointReturns() {
        stubFor(get(urlEqualTo("/pippo"))
//                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("aleee aleee gioachino aleee")));
    }

}
