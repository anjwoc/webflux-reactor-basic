package me.study.webfluxreactorbasic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WebfluxReactorBasicApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void createAFlux_just() throws Exception {
        Flux<String> fruitFlux = Flux
                .just("Apple", "Oragne", "Grape", "Banana", "Strawberry");

        fruitFlux.subscribe(System.out::println);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Oragne")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromArray() throws Exception {
        // given
        String[] fruits = new String[] { "Apple", "Orange", "Grape", "Banana", "Strawberry" };

        // when
        Flux<String> fruitFlux = Flux.fromArray(fruits);

        // then
        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void flatMap() throws Exception {
        Flux<Player> playerFlux = Flux
                .just("Micheal Jordan", "Scottie Pippen", "Steve kerr")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] splited = p.split("\\s");
                            return new Player(splited[0], splited[1]);
                        })
                )
                .subscribeOn(Schedulers.parallel());

        List<Player> players = Arrays.asList(
                new Player("Micheal", "Jordan"),
                new Player("Scottie", "Pipeen"),
                new Player("Steve", "Kerr")
        );

        StepVerifier.create(playerFlux)
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .verifyComplete();
    }

}
