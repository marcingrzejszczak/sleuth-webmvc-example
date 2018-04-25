package sleuth.webmvc.frontend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
@Retryable
public class FrontendConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
