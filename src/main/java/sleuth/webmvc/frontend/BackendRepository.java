package sleuth.webmvc.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Repository
public class BackendRepository {

    RestTemplate restTemplate;

    public BackendRepository(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Async
    @Retryable(backoff = @Backoff(delay = 1000L))
    public Future<String> callingBackendAsync() {
        return new AsyncResult<>(restTemplate.getForObject("http://localhost:9000/api", String.class));
    }
}
