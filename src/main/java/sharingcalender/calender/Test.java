package sharingcalender.calender;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Test {

    @Value("${db_host:NOT_FOUND}")
    private String dbHost;

    @PostConstruct
    public void checkConfig() {
        System.out.println("DB_HOST: " + dbHost);
    }
}
