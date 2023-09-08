package mini_project.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {
    
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> template;

    public void addToCart(String customerId, String serviceId) {
        System.out.println("adding to cart...");
        Integer quantity = 1;
        String q = (String) template.opsForHash().get(customerId, serviceId);
        
        if (q != null)
        quantity += Integer.parseInt(q);
        System.out.println("quantity..." + quantity);

        template.opsForHash().put(customerId, serviceId.toString(), quantity.toString());
    }

    public Optional<Map<Object, Object>> getCart(String customerId) {

        if (!template.hasKey(customerId))
            return Optional.empty();

        Map<Object, Object> cart = new HashMap<>();

        Set<Object> keys = template.opsForHash().keys(customerId);
        for (Object k: keys)
            cart.put(k, template.opsForHash().get(customerId, k));            

        return Optional.of(cart);
    }
}
