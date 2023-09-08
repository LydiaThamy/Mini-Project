package mini_project.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    
    Integer serviceId;
    String title;
    String description;
    Double price;
    
}
