package pl.cru;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
class CarRentEvent {
    private Car car;
    private String user;
    private LocalDateTime eventDate;
}
