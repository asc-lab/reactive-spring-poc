package pl.cru;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Stream;

@CrossOrigin
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarReactiveRepository repository; // reactive mongo repository implementation

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Car> findAll() {
        return repository.findAll().delayElements(Duration.ofMillis(1000));
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Car> findById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping(value = "/all-transient", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Car> findAllTransient() {
        return repository.findAllBy().delayElements(Duration.ofMillis(1000));
    }

    @GetMapping(value = "/delayed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Car> cars() {
        return repository.findAll().delayElements(Duration.ofMillis(100));
    }

    // simulate blocking api
    @GetMapping(value = "/{id}/delayed/{delayInMilliseconds}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Car> delayedById(@PathVariable String id, @PathVariable Long delayInMilliseconds) {
        return repository.findById(id).delayElement(Duration.ofMillis(delayInMilliseconds));
    }

    @GetMapping(value = "/{id}/rentals", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CarRentEvent> nestedStreams(@PathVariable String id) {
        Car car = repository.findById(id).block();
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1)); // simulate delay
        Flux<CarRentEvent> events = Flux.fromStream(Stream.generate(() -> new CarRentEvent(car,
                randomUser(),
                LocalDateTime.now())));
        return Flux.zip(interval, events).map(Tuple2::getT2);
    }

    private String randomUser() {
        String[] users = "U1,U2,U3,U4,U5,U6".split(",");
        return users[new Random().nextInt(users.length)];
    }
}
