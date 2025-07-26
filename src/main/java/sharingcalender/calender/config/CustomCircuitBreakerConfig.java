package sharingcalender.calender.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomCircuitBreakerConfig {

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {

        return CircuitBreakerConfig.custom()
            // 실패율 임계값을 백분율로 설정, 실패율이 임계값 이상이 되면 CircuitBreaker 가 OPEN 상태로 전환
            .failureRateThreshold(100)

            // 느린 호출 비율 임계값을 백분율로 설정, 호출 시간이 slowCallDurationThreshold보다 길면 느린 호출로 간주
            // 느린 호출 비율이 이 임계값 이상이 되면 CircuitBreaker가 OPEN 상태로 전환
            .slowCallRateThreshold(100)
            //TODO 지속시간 수정필요, 디버깅위해 임시로 길게 설정함 , 5초로 설정할 것
            // 느린 호출로 판단할 최대 지속시간을 밀리초 단위로 설정
            .slowCallDurationThreshold(Duration.ofSeconds(5))

            // HALF_OPEN 상태일 때 허용되는 최대 호출 횟수를 설정
            .permittedNumberOfCallsInHalfOpenState(1)

            // 에러율 또는 느린 호출 비율을 계산하기 위해 요구되는 최소 호출 수를 설정
            .minimumNumberOfCalls(1)
            .slidingWindowSize(1)

            //TODO 대기시간 수정필요, 디버깅위해 임시로 짧게 설정함, 16분으로 재설정해야함
            // OPEN 상태에서 HALF_OPEN 상태로 전환하기 전에 대기할 시간을 밀리초 단위로 설정
            .waitDurationInOpenState(Duration.ofMinutes(16))
            .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.of(circuitBreakerConfig());
    }

//    @Bean
//    public CircuitBreaker circuitBreaker() {
//        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig());
//
//        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("calendar-event");
////        circuitBreaker.getEventPublisher()
////            .onError(event -> circuitBreaker.transitionToOpenState());
//        return circuitBreaker;
//    }

}
