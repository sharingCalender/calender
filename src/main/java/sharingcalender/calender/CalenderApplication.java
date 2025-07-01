package sharingcalender.calender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
@EnableFeignClients
public class CalenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalenderApplication.class, args);
    }

}
