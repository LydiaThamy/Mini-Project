package mini_project.server.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public static final String GET_CART_SQL_BY_SERVICE_ID = "select b.business_name, s.title from service as s join business as b on b.business_id = s.business_id where s.service_id = ?";

        public Map<String, Object> getCartByServiceId(String serviceId) {
        return jdbcTemplate.queryForMap(GET_CART_SQL_BY_SERVICE_ID, serviceId);
    }

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
        for (Object key: redisTemplate.opsForHash().keys(customerId)) {
            redisTemplate.opsForHash().delete(customerId, key);
            // redisTemplate.opsForHash().delete(customerId, redisTemplate.opsForHash().keys(customerId));
        }

        for (Map<String, String> item : cart)
            redisTemplate.opsForHash().put(customerId, item.get("serviceId"), item.get("quantity"));
    }
}
