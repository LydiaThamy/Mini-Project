package mini_project.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search {
    
    String keyword;
    String[] category;
    String[] region;
}
