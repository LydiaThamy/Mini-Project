package mini_project.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    
    Integer businessId;
    String businessName;
    String address;
    String phone;
    String email;
    String website;
    String logo;
    String region;
    String category;

}
