package sharingcalender.calender.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import sharingcalender.calender.handler.CustomHandShakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${websocket.url}")
    private String websocketURL;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/connect")
            .setAllowedOrigins(websocketURL)
            .setHandshakeHandler(new CustomHandShakeHandler())
            .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 정의된 prefix 를 가지는 destination 의 경우, @MessageMapping 애노테이션 기반 메서드에서 처리
        // 단, @MessageMapping 에 경로를 작성할 때 prefix 를 제외하고 작성해야한다.
        registry.setApplicationDestinationPrefixes("/publish");

        registry.enableSimpleBroker("/topic","/queue");

        registry.setUserDestinationPrefix("/user");
    }
}
