package mini_project.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    
    // the product name
	private String name;
	//  currency like usd, eur ...
	private String currency;
	// our success and cancel url stripe will redirect to this links
	private String successUrl;
	private String cancelUrl;
	private long amount;
	private long quantity;
}
