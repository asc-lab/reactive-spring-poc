package pl.cru;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

@Slf4j
@Service
@AllArgsConstructor
public class CarService {

    private final WebClient client;

    void nestedRestCall() {
        client.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Car.class)
                .take(80) // !!
                .filter(car -> car.getId().equals("5"))
                .doOnEach(e -> log.info(e.toString()))
                .flatMap(car ->
                        client.get()
                                .uri("/{id}/rentals", car.getId())
                                .retrieve()
                                .bodyToFlux(CarRentEvent.class)
                                .doOnEach(e -> log.info(e.toString()))
                                .take(20)
                )
                .subscribe(e -> log.info(e.toString()));
    }

    void multipleRestCall() {
        Flux.just(
                restCall("1", 2000L),
                restCall("2", 4000L),
                restCall("3", 5000L),
                restCall("4", 10000L))
                .flatMap(Flux::collectList)
                .timeout(Duration.ofMillis(4500), Flux.error(new TimeoutException("Timeout!"), true))
                .doOnError(e -> System.out.println("ERROR " + e.getMessage()))
                .subscribe(e -> log.info(e.toString()));
    }

    private Flux<Car> restCall(String id, Long delay) {
        return client.get().uri("/" + id + "/delayed/" + delay).retrieve().bodyToFlux(Car.class);
    }

    void insertNewCars(int i) {
        IntStream.range(0, i).forEach(e -> {
            client.post().uri("").retrieve().bodyToMono(Car.class)
                    .subscribe(x -> log.info(x.toString()));
        });
    }
}
