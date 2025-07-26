package sharingcalender.calender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import sharingcalender.calender.service.chat.RedisPubSubService;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisCacheWriter redisCacheWriter(){
        return RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory());
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);

        redisConfig.setPassword(password);

        return new LettuceConnectionFactory(redisConfig);
    }

    // publish 객체
    @Bean(name = "redisWriteTemplate")
    public StringRedisTemplate redisWriteTemplate(RedisConnectionFactory redisConnectionFactory) {

        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);

//        stringRedisTemplate.setEnableTransactionSupport(true);

        return stringRedisTemplate;
    }

    @Bean(name = "redisReadTemplate")
    public StringRedisTemplate redisReadTemplate(RedisConnectionFactory redisConnectionFactory) {

        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory redisConnectionFactory,
        MessageListenerAdapter messageListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic("chat"));

        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisPubSubService redisPubSubService) {
        return new MessageListenerAdapter(redisPubSubService, "onMessage");
    }


}
