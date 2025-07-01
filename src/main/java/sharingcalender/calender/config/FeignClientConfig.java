package sharingcalender.calender.config;

import feign.Client;
import feign.Logger;
import feign.Logger.Level;
import feign.auth.BasicAuthRequestInterceptor;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Value("${mailgun.api-key}")
    private String mailgunApiKey;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("api",mailgunApiKey);
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Level.FULL;
    }
    @Bean
    public CloseableHttpClient closeableHttpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public Client client(CloseableHttpClient closeableHttpClient) {
        return new ApacheHttpClient(closeableHttpClient);
    }
}
