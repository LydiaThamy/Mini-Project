package mini_project.server.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    
    Integer reviewId;
    String reviewer;
    String content;
    Integer rating;
    Date reviewDate;

}
