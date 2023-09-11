package mini_project.server.repository;

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
public class CartRepository {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public void addToCart(String customerId, String serviceId) {
        Integer quantity = 1;

        String q = (String) redisTemplate.opsForHash().get(customerId, serviceId);
        if (q != null)
            quantity += Integer.parseInt(q);

        redisTemplate.opsForHash().put(customerId, serviceId.toString(), quantity.toString());
    }

    public Optional<Map<Object, Object>> getCart(String customerId) {

        if (!redisTemplate.hasKey(customerId))
            return Optional.empty();

        Map<Object, Object> cart = new HashMap<>();
        Set<Object> keys = redisTemplate.opsForHash().keys(customerId);
        for (Object k : keys) {
            cart.put(k, redisTemplate.opsForHash().get(customerId, k));
        }

        return Optional.of(cart);
    }

    public void updateCart(String customerId, List<Map<String, String>> cart) {
        // delete all hashes in customerId
        for (Object key : redisTemplate.opsForHash().keys(customerId)) {
            redisTemplate.opsForHash().delete(customerId, key);
            // redisTemplate.opsForHash().delete(customerId,
            // redisTemplate.opsForHash().keys(customerId));
        }

        for (Map<String, String> item : cart)
            redisTemplate.opsForHash().put(customerId, item.get("serviceId"), item.get("quantity"));
    }

    public void deleteItem(String customerId, String serviceId) {
        redisTemplate.opsForHash().delete(customerId, serviceId);
    }
}
