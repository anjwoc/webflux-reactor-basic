package me.study.webfluxreactorbasic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
    public void createAFlux_fromIterable() throws Exception {
        // given
        ArrayList<String> fruitList = new ArrayList<>();
        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");

        // when
        Flux<String> fruitFlux = Flux.fromIterable(fruitList);

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
    public void createAFlux_fromStream() throws Exception {
        // given
        Stream<String> streams = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        // when
        Flux<String> stringFlux = Flux.fromStream(streams);

        // then
        StepVerifier.create(stringFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_range() throws Exception {
        Flux<Integer> range = Flux.range(1, 5);

        StepVerifier.create(range)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void createAFlux_interval() throws Exception {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1))
                .take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    // Reactive Type 조합하기
    @Test
    public void mergeFluxes() throws Exception {
        // given
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
            .just("Lasagna", "Lollipops", "Apples")
            .delaySubscription(Duration.ofMillis(250))
            .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
            .expectNext("Garfield")
            .expectNext("Lasagna")
            .expectNext("Kojak")
            .expectNext("Lollipops")
            .expectNext("Barbossa")
            .expectNext("Apples")
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
