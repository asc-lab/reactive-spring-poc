package pl.cru.examples;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.MapEntry;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Slf4j
public class FluxTest {

    @Test
    public void fluxIsLazy() {
        Flux<Integer> flux = Flux.create(subscriber -> {
            log.info("Started emitting but sleeping for 5 secs"); //this is not executed
            subscriber.next(1);
        });
        log.info("Finished");
    }

    @Test
    public void fluxContact() {
        Flux.concat(
                Flux.just(1, 2, 4),
                Flux.just("A", "B")
        )
                .subscribe(e -> log.info(e.toString()));
    }

    @Test // trhead.sleep vs. stepVerifier
    public void fluxInterval() throws InterruptedException {
        Flux<String> colors = Flux.just("red", "green", "blue");
        Flux<Long> timer = Flux.interval(Duration.ofSeconds(2));

        Flux<String> zip = Flux.zip(colors, timer, (key, val) -> key);

        zip.subscribe(log::info);

//        StepVerifier.create(zip)
//                .expectNext("red")
//                .expectNext("green")
//                .expectNext("blue")
//                .expectComplete()
//                .verify(Duration.ofSeconds(7));

        Thread.sleep(9000);
    }

    @Test
    public void subscribeFlux() {
        Flux<String> x = Flux.fromArray("1,2,3,4,5,6,7".split(","));
        x.map(Integer::parseInt)
                .filter(i -> i % 2 == 0)
                .subscribe(e -> log.info(e.toString()));
        Publisher p = x;
    }

    @Test
    public void subscribeMono() {
        Mono<String> x = Mono.just("1");
        x.map(Integer::parseInt)
                .log()
                .subscribe(e -> log.info(e.toString()));
    }


    @Test
    public void fluxZip() {
        Flux<Integer> integerFlux = Flux.just(1, 2, 4);
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E");

        Flux.zip(integerFlux, stringFlux, (integer, s) -> integer + "-" + s)
                .subscribe(log::info);

    }

    @Test
    public void flatMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E");
        stringFlux.flatMap(s -> Flux.just(s + new Random().nextInt()))
                .subscribe(log::info);
    }

    @Test
    public void groupBy() {
        Flux<String> colors = Flux.fromArray(new String[]{"red", "green", "blue", "red", "yellow", "green", "green"});

        Flux<GroupedFlux<String, String>> groupedColorsStream = colors
                .groupBy(val -> val); //identity function

        groupedColorsStream
                .flatMap(groupedColor -> groupedColor
                        .count()
                        .map(count -> MapEntry.entry(groupedColor.key(), count))
                )
                .subscribe(e -> log.info(e.getKey() + " = " + e.getValue()));
    }

}
