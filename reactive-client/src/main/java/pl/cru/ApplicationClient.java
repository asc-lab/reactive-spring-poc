package pl.cru;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ApplicationClient {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient.class, args);
    }

    @Bean
    WebClient client() {
        return WebClient.create("http://localhost:7575/cars");
    }

    @Bean
    @Profile("One")
    CommandLineRunner nestedRestCall(CarService carService) {
        return args -> carService.nestedRestCall();
    }

    @Bean
    @Profile("Two")
    CommandLineRunner multipleRestCall(CarService carService) {
        return args -> carService.multipleRestCall();
    }

    @Bean
    @Profile("Three")
    CommandLineRunner insertingNewCars(CarService carService) {
        return args -> carService.insertNewCars(10);
    }


}


