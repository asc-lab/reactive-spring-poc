package pl.cru;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

interface CarReactiveRepository extends ReactiveMongoRepository<Car, String> {

    @Tailable
    Flux<Car> findAllBy();
}
