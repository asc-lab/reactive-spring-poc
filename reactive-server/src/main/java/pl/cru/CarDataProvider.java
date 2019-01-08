package pl.cru;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class CarDataProvider implements ApplicationListener<ContextRefreshedEvent> {

    private final CarReactiveRepository carReactiveRepository;
    private final MongoOperations mongo;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        mongo.dropCollection(Car.class);
        mongo.createCollection(Car.class, CollectionOptions.empty().size(1000000).capped());

        IntStream.range(1, 15)
                .forEach(e -> carReactiveRepository.save(new Car(e + "", UUID.randomUUID().toString())).block());
    }

}
