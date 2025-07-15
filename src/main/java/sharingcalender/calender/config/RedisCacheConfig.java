package sharingcalender.calender.config;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CachePutOperation;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheStatistics;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import sharingcalender.calender.dto.calendar.response.CalendarGroupListResponseDto;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {

    private final RedisCacheWriter redisCacheWriter;

    public static final String groupCacheName= "group";
    public static final String calendarCacheName= "calendar";

    @Bean(name = "groupCache")
    public RedisCacheConfiguration groupCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(
                        new Jackson2JsonRedisSerializer<>(CalendarGroupListResponseDto.class)
                    )
            )
            .entryTtl(Duration.ofHours(1));
    }


    @Bean(name = "calendarCache")
    public RedisCacheConfiguration calendarCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(
                        new Jackson2JsonRedisSerializer<>(CalendarGroupListResponseDto.class)
                    )
            )
            .entryTtl(Duration.ofHours(1));
    }


    @Primary
    @Bean(name = "cacheableCacheManager")
    public RedisCacheManager cacheableManager(){
        return RedisCacheManager.builder()
            .cacheWriter(new PutToPutIfAbsentRedisCacheWriterProxy(redisCacheWriter))
            .withCacheConfiguration(groupCacheName,groupCacheConfiguration())
            .withCacheConfiguration(calendarCacheName,calendarCacheConfiguration())
            .build();
    }

    @Bean(name = "cachePutCacheManager")
    public RedisCacheManager cachePutManager() {
        return RedisCacheManager.builder()
            .cacheWriter(redisCacheWriter)
            .withCacheConfiguration(groupCacheName,groupCacheConfiguration())
            .withCacheConfiguration(calendarCacheName,calendarCacheConfiguration())
            .build();
    }

    @Bean
    public CacheResolver cacheResolver() {
        CacheManager cacheableManager = cacheableManager();
        CacheManager cachePutManager = cachePutManager();

        return context -> {
            Collection<Cache> caches = new ArrayList<>();
            if (context.getOperation() instanceof CachePutOperation) {
                caches.add(
                    cachePutManager.getCache(
                        context.getOperation().getCacheNames().iterator().next()
                    )
                );
            } else {
                caches.add(
                    cacheableManager.getCache(
                        context.getOperation().getCacheNames().iterator().next()
                    )
                );

            }

            return caches;
        };

    }


    @RequiredArgsConstructor
    private class PutToPutIfAbsentRedisCacheWriterProxy implements RedisCacheWriter {

        private final RedisCacheWriter redisCacheWriter;

        @Override
        public byte[] get(String name, byte[] key) {
            return redisCacheWriter.get(name, key);
        }

        @Override
        public CompletableFuture<byte[]> retrieve(String name, byte[] key, Duration ttl) {
            return redisCacheWriter.retrieve(name, key, ttl);
        }

        @Override
        public void put(String name, byte[] key, byte[] value, Duration ttl) {
            redisCacheWriter.putIfAbsent(name, key, value, ttl);
        }

        @Override
        public CompletableFuture<Void> store(String name, byte[] key, byte[] value, Duration ttl) {
            return redisCacheWriter.store(name, key, value, ttl);
        }

        @Override
        public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
            return redisCacheWriter.putIfAbsent(name,key,value,ttl);
        }

        @Override
        public void remove(String name, byte[] key) {
            redisCacheWriter.remove(name, key);
        }

        @Override
        public void clean(String name, byte[] pattern) {
            redisCacheWriter.clean(name, pattern);
        }

        @Override
        public void clearStatistics(String name) {
            redisCacheWriter.clearStatistics(name);

        }

        @Override
        public RedisCacheWriter withStatisticsCollector(
            CacheStatisticsCollector cacheStatisticsCollector) {
            return redisCacheWriter.withStatisticsCollector(cacheStatisticsCollector);
        }

        @Override
        public CacheStatistics getCacheStatistics(String cacheName) {
            return redisCacheWriter.getCacheStatistics(cacheName);
        }
    }
}
