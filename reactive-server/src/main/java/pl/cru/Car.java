package pl.cru;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@AllArgsConstructor
@Data
class Car {
    @Id
    private String id;
    private String description;

    Car() {
        this.description =  UUID.randomUUID().toString();
    }
}
