package mini_project.server.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.user}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);

        if (!redisUsername.isEmpty() && !redisPassword.isEmpty()) {
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        config.setDatabase(0);
        final JedisClientConfiguration jedisClient = JedisClientConfiguration
                .builder()
                .build();

        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config,
                jedisClient);
        jedisFac.afterPropertiesSet();

        RedisTemplate<String, Object> r = new RedisTemplate<>();
        r.setConnectionFactory(jedisFac);
        r.setKeySerializer(new StringRedisSerializer());
        r.setHashKeySerializer(new StringRedisSerializer());
        r.setHashValueSerializer(new StringRedisSerializer());

        return r;
    }
}
